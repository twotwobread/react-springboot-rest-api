package org.prgrms.be.app.domain.product.repository;

import org.prgrms.be.app.domain.product.CategoryType;
import org.prgrms.be.app.domain.product.Product;
import org.prgrms.be.app.exception.DatabaseFindException;
import org.prgrms.be.app.exception.DatabaseUpdateException;
import org.prgrms.be.app.util.ConvertUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ProductJdbcMysqlRepository implements ProductRepository {

    private final static RowMapper<Product> productRowMapper = (resultSet, i) -> mapToProduct(resultSet);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProductJdbcMysqlRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Product mapToProduct(ResultSet resultSet) throws SQLException {
        UUID productId = ConvertUtil.toUUID(resultSet.getBytes("product_id"));
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        CategoryType category = CategoryType.of(resultSet.getString("category"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        UUID brandId = ConvertUtil.toUUID(resultSet.getBytes("brand_id"));
        int stockQuantity = resultSet.getInt("stock_quantity");

        return new Product(productId, name, brandId, price, category, createdAt, stockQuantity);
    }

    @Override
    public Product insert(Product product) {
        int insertCnt = jdbcTemplate.update("INSERT INTO product (product_id, name, price ,category, created_at, brand_id, stock_quantity)" +
                        " VALUES (UNHEX(REPLACE(:productId,'-','')), :name, :price ,:category, :createdAt, UNHEX(REPLACE(:brandId,'-','')), :stockQuantity)",
                toParamMap(product));

        if (insertCnt != 1) {
            throw new DatabaseUpdateException("product: insert가 수행되지 않았습니다.");
        }

        return product;
    }

    @Override
    public UUID delete(UUID productId) {
        int deleteCnt = jdbcTemplate.update("DELETE FROM product WHERE product_id = UNHEX(REPLACE(:productId, '-', ''))",
                Collections.singletonMap("productId", productId.toString()));

        if (deleteCnt != 1) {
            throw new DatabaseUpdateException("product: delete가 수행되지 않았습니다.");
        }

        return productId;
    }

    @Override
    public Product update(Product product) {
        int updateCnt = jdbcTemplate.update("UPDATE product SET price=:price, category=:category, stock_quantity=:stockQuantity, name=:name WHERE product_id = UNHEX(REPLACE(:productId,'-',''))",
                toParamMap(product));

        if (updateCnt != 1) {
            throw new DatabaseUpdateException("product: update가 수행되지 않았습니다.");
        }

        return product;
    }


    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM product WHERE product_id = UNHEX(REPLACE(:productId,'-',''))",
                    Collections.singletonMap("productId", productId.toString()),
                    productRowMapper));
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseFindException("product: 해당 id를 가진 product가 존재하지 않습니다.", e);
        }
    }

    @Override
    public List<Product> findByBrandId(UUID brandId) {
        return jdbcTemplate.query("SELECT * FROM product WHERE brand_id = UNHEX(REPLACE(:brandId,'-',''))",
                Collections.singletonMap("brandId", brandId.toString()),
                productRowMapper);
    }

    @Override
    public List<Product> findProductsByName(String name) {
        return jdbcTemplate.query("SELECT * FROM product WHERE name=:name",
                Collections.singletonMap("name", name),
                productRowMapper);
    }

    @Override
    public List<Product> getAllProduct() {
        return jdbcTemplate.query("SELECT * FROM product",
                Collections.emptyMap(),
                productRowMapper);
    }

    @Override
    public List<Product> findByBrandIdAndCategory(UUID brandId, CategoryType category) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM product WHERE 1=1");

        if (brandId != null) {
            map.put("brandId", brandId.toString());
            sb.append(" AND brand_id = UNHEX(REPLACE(:brandId,'-',''))");
        }
        if (category != null) {
            map.put("category", category.getTitle());
            sb.append(" AND category = :category");
        }

        return jdbcTemplate.query(sb.toString(), map, productRowMapper);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM product", Collections.emptyMap());
    }

    private Map<String, Object> toParamMap(Product product) {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", product.getId().toString());
        map.put("name", product.getName());
        map.put("price", product.getPrice());
        map.put("category", product.getCategory().getTitle());
        map.put("createdAt", product.getCreatedAt());
        map.put("brandId", product.getBrandId().toString());
        map.put("stockQuantity", product.getStockQuantity());

        return map;
    }
}
