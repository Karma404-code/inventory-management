package org.example.product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    Connection conn;

    ProductRepository() {
        String url = "jdbc:h2:mem:inventory;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:h2init.sql';USER=admin;PASSWORD=admin";
        String user = "admin";
        String password = "admin";
        try {
            conn =  DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> findAll() {
        PreparedStatement ps;
        List<Product> products = new ArrayList<>();
        try {
            ps = conn.prepareStatement("select * from product");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"));
                products.add(p);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public Optional<Product> findById(long id) {
        PreparedStatement ps;
        Optional<Product> product = Optional.empty();
        try {
            ps = conn.prepareStatement("select * from product where id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product = Optional.of(new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public void addProduct(Product product) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("INSERT INTO product (name, price, quantity) " +
                    "VALUES (?, ?, ?)");
            ps.setString(1, product.name());
            ps.setDouble(2, product.price());
            ps.setInt(3, product.quantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProductById(long id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM product WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProductById(Product product) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE product SET name = ?, price = ?, quantity = ? WHERE id = ?");
            ps.setString(1, product.name());
            ps.setDouble(2, product.price());
            ps.setInt(3, product.quantity());
            ps.setLong(4, product.id());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> sortProductByColumn(String column) {
        List<Product> products = new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("select * from product order by %s asc ".formatted(column));

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                products.add(new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
}
