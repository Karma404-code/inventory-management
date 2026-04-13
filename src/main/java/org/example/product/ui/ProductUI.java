package org.example.product.ui;

import net.miginfocom.swing.MigLayout;
import org.example.authenticate.ui.Login;
import org.example.product.Product;
import org.example.product.ProductService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    JButton logout, add;
    JPanel buttonPanel;
    JLabel sortLabel;
    DefaultComboBoxModel<String> sortModel;
    JComboBox<String> sort;

    ProductService productService;

    public ProductUI(JFrame frame) {
        this.parentFrame = frame;
        productService = ProductService.getInstance();

        init();

        add(productScroll, BorderLayout.CENTER);
    }

    private void init() {
        setLayout(new BorderLayout());

        Object[] cols = {"id", "Name", "Price", "Quantity"};
        productModel = new DefaultTableModel(cols, 0);

        productTable = new JTable(productModel);
        setupTable();
        productTable.removeColumn(productTable.getColumnModel().getColumn(0));

        productScroll = new JScrollPane(productTable);
        productScroll.setBorder(new EmptyBorder(10, 25, 25, 25));
        centerRowValue();
        refreshTable();

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new MigLayout());
        buttonPanel.setPreferredSize(new Dimension(500, 80));
        buttonPanel.setBorder(new EmptyBorder(10, 35, 15, 25));

        add = new JButton("Add");
        add.addActionListener(e -> {
            addProduct();
        });

        sortLabel = new JLabel("Sort:");
        sortModel = new DefaultComboBoxModel<String>();
        IntStream.range(0, productModel.getColumnCount()).forEach(i -> {
           sortModel.addElement((String) productModel.getColumnName(i));
        });
        sort = new JComboBox<>(sortModel);

        sort.addActionListener(e -> {
            String selected = (String) sortModel.getSelectedItem();
            System.out.println(selected);
            sortTable(selected);
        });

        logout = new JButton("Logout");
        logout.addActionListener(e -> {
            parentFrame.remove(this);
            parentFrame.add(new Login(parentFrame));
            parentFrame.revalidate();
            parentFrame.repaint();
        });


        buttonPanel.add(add);
        buttonPanel.add(sortLabel);
        buttonPanel.add(sort);
        buttonPanel.add(logout, "cell 80 0");


        add(buttonPanel, BorderLayout.NORTH);
        add(productScroll,  BorderLayout.CENTER);

    }

    private void sortTable(String option) {
           productModel.setRowCount(0);
           productService.sortProduct(option.toLowerCase()).forEach(product ->
                productModel.addRow( new Object[]{
                        product.id(),
                        product.name(),
                        product.price(),
                        product.quantity()
                }
        ));
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
        String productName = (String) productModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(productTable,
                "Are you sure you want to delete '" + productName + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            long id = (long) productModel.getValueAt(row, 0);
            productService.removeProduct(id);
            // System.out.println(productModel.getValueAt(row, 0));
            refreshTable();
            JOptionPane.showMessageDialog(productTable, "Product deleted successfully!");
        }
    }

    private void editProduct(int row) {
        String productName = (String) productModel.getValueAt(row, 1);
        double price = (double) productModel.getValueAt(row, 2);
        int quantity = (int) productModel.getValueAt(row, 3);

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
                long id = (long) productModel.getValueAt(row, 0);
                String newName = nameField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                int newQuantity = Integer.parseInt(quantityField.getText());
                 System.out.println("editing: " + id);
                // Update the product
                productService.updateProduct(new Product(id, newName, newPrice, newQuantity));
                refreshTable();
                editDialog.dispose();

                JOptionPane.showMessageDialog(productTable, "Product updated successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(productTable, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(ev -> {
            editDialog.dispose();
        });

        editDialog.pack();
        editDialog.setLocationRelativeTo(productTable);
        editDialog.setVisible(true);
    }

    private void addProduct() {

        // Create edit dialog
        JDialog addDialog = new JDialog(parentFrame, "Add Product", true);
        addDialog.setLayout(new FlowLayout());

        addDialog.add(new JLabel("Name:"));

        JTextField nameField = new JTextField();
        addDialog.add(nameField);

        // Price field

        addDialog.add(new JLabel("Price:"));

        JTextField priceField = new JTextField();
        addDialog.add(priceField);

        // Quantity field

        addDialog.add(new JLabel("Quantity:"));

        JTextField quantityField = new JTextField();
        addDialog.add(quantityField);

        // Button.gridx = 0.gridy = 3;
        JButton saveButton = new JButton("Save");
        addDialog.add(saveButton);
        JButton cancelButton = new JButton("Cancel");
        addDialog.add(cancelButton);

        saveButton.addActionListener(ev -> {
            try {
                 try {
                String newName = nameField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                int newQuantity = Integer.parseInt(quantityField.getText());
                // add the product
                productService.addProduct(new Product(0, newName, newPrice, newQuantity));
                refreshTable();
                addDialog.dispose();

                JOptionPane.showMessageDialog(parentFrame, "Product added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(productTable, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(ev -> {
            addDialog.dispose();
        });

        addDialog.pack();
        addDialog.setLocationRelativeTo(productTable);
        addDialog.setVisible(true);
    }

    private void centerRowValue() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        IntStream.range(0, productModel.getRowCount()).forEach(i ->
                productTable.getColumnModel()
                .getColumn(i)
                .setCellRenderer(centerRenderer));

    }

    public void refreshTable() {
        productModel.setRowCount(0);
        productService.getAllProducts().forEach(product ->
                productModel.addRow( new Object[]{
                        product.id(),
                        product.name(),
                        product.price(),
                        product.quantity(),
                }
        ));
    }
}
