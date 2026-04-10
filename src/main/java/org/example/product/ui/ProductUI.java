package org.example.product.ui;

import org.example.product.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.IntStream;

public class ProductUI extends JPanel {
    JFrame parentFrame;
    DefaultTableModel productModel;
    JTable productTable;
    JScrollPane productScroll;

    ProductService productService;

    public ProductUI(JFrame frame) {
        this.parentFrame = frame;
        productService = ProductService.getInstance();

        init();

        add(productScroll, BorderLayout.CENTER);
    }

    private void init() {
        setLayout(new BorderLayout());

        Object[] cols = {"Product", "Price", "Quantity", "isEdited"};
        productModel = new DefaultTableModel(cols, 0);

        productTable = new JTable(productModel);
        setupTable();

        productScroll = new JScrollPane(productTable);

        centerRowValue();
        refreshTable();

        add(productScroll,  BorderLayout.CENTER);
    }

    private void setupTable() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        productTable.setDefaultRenderer(Object.class, centerRenderer );
        productTable.setDefaultEditor(Object.class, null);

         // Add mouse listener for right-click menu
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = productTable.rowAtPoint(e.getPoint());
                    // int col = productTable.columnAtPoint(e.getPoint());

                    showPopupMenu(e, row);
                }
            }
        });
    }

    private void showPopupMenu(MouseEvent e, int row) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");

        editItem.addActionListener(ev -> editProduct(row));
        deleteItem.addActionListener(ev -> deleteProduct(row));

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private void deleteProduct(int row) {
        String productName = (String) productModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(productTable,
                "Are you sure you want to delete '" + productName + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
           //  productService.deleteProduct(row);
            refreshTable();
            JOptionPane.showMessageDialog(productTable, "Product deleted successfully!");
        }
        System.out.println("deleting: " + row);
    }

    private void editProduct(int row) {
        productTable.setValueAt(true, row, 3);

        String productName = (String) productModel.getValueAt(row, 0);
        double price = (double) productModel.getValueAt(row, 1);
        int quantity = (int) productModel.getValueAt(row, 2);

        // Create edit dialog
        JDialog editDialog = new JDialog(parentFrame, "Edit Product", true);
        editDialog.setLayout(new FlowLayout());

        editDialog.add(new JLabel("Name:"));

        JTextField nameField = new JTextField(productName, 15);
        editDialog.add(nameField);

        // Price field

        editDialog.add(new JLabel("Price:"));

        JTextField priceField = new JTextField(String.valueOf(price), 15);
        editDialog.add(priceField);

        // Quantity field

        editDialog.add(new JLabel("Quantity:"));

        JTextField quantityField = new JTextField(String.valueOf(quantity), 15);
        editDialog.add(quantityField);

        // Button.gridx = 0.gridy = 3;
        JButton saveButton = new JButton("Save");
        editDialog.add(saveButton);
        JButton cancelButton = new JButton("Cancel");
        editDialog.add(cancelButton);

        saveButton.addActionListener(ev -> {
            try {
                String newName = nameField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                int newQuantity = Integer.parseInt(quantityField.getText());

                // Update the product
                // productService.updateProduct(row, newName, newPrice, newQuantity);

                // Reset isEdited to false after saving
                productModel.setValueAt(false, row, 3);
                // productService.setProductEdited(row, false);

                refreshTable();
                editDialog.dispose();

                JOptionPane.showMessageDialog(productTable, "Product updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(productTable, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(ev -> {
            // Reset isEdited to false if user cancels
            productModel.setValueAt(false, row, 3);
            // productService.setProductEdited(row, false);
            editDialog.dispose();
        });

        editDialog.pack();
        editDialog.setLocationRelativeTo(productTable);
        editDialog.setVisible(true);
        System.out.println("editing: " + row);
    }

    private void addProduct() {
        // todo
    }

    private void centerRowValue() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        IntStream.range(0, productModel.getRowCount()).forEach(i -> {
           productTable.getColumnModel()
                   .getColumn(i)
                   .setCellRenderer(centerRenderer);
        });

    }

    private void refreshTable() {
        productModel.setRowCount(0);
        productService.getAllProducts().forEach(product ->
                productModel.addRow( new Object[]{
                        product.name(),
                        product.price(),
                        product.quantity(),
                        product.isEdited()
                }
        ));
    }
}
