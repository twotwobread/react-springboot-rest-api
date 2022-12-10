package org.prgrms.be.app.domain.order_product.repository;

import org.prgrms.be.app.domain.order_product.OrderProduct;
import org.prgrms.be.app.exception.DatabaseUpdateException;
import org.prgrms.be.app.util.ConvertUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class OrderProductJdbcMysqlRepository implements OrderProductRepository {

    private final static RowMapper<OrderProduct> orderProductRowMapper = (resultSet, i) -> mapToOrderProduct(resultSet);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderProductJdbcMysqlRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static OrderProduct mapToOrderProduct(ResultSet resultSet) throws SQLException {
        UUID orderProductId = ConvertUtil.toUUID(resultSet.getBytes("order_product_id"));
        UUID orderId = ConvertUtil.toUUID(resultSet.getBytes("order_id"));
        UUID productId = ConvertUtil.toUUID(resultSet.getBytes("product_id"));
        int orderPrice = resultSet.getInt("order_price");
        int count = resultSet.getInt("count");

        return new OrderProduct(orderProductId, orderId, productId, orderPrice, count);
    }

    @Override
    public OrderProduct insert(OrderProduct orderProduct) {
        int insertCnt = jdbcTemplate.update("INSERT INTO order_product_list (order_product_id, order_id, product_id, order_price, count)" +
                        " VALUES(UNHEX(REPLACE(:orderProductId,'-','')), UNHEX(REPLACE(:orderId,'-','')),  UNHEX(REPLACE(:productId,'-','')), :orderPrice, :count)",
                toParamMap(orderProduct));

        if (insertCnt != 1) {
            throw new DatabaseUpdateException("orderProduct: insert가 수행되지 않았습니다.");
        }

        return orderProduct;
    }

    @Override
    public UUID delete(UUID orderProductId) {
        int deleteCnt = jdbcTemplate.update("DELETE FROM order_product_list WHERE order_product_id = UNHEX(REPLACE(:orderProductId,'-',''))",
                Collections.singletonMap("orderProductId", orderProductId.toString()));

        if (deleteCnt != 1) {
            throw new DatabaseUpdateException("orderProduct: delete가 수행되지 않았습니다.");
        }

        return orderProductId;
    }

    @Override
    public List<OrderProduct> findbyOrderId(UUID orderId) {
        return jdbcTemplate.query("SELECT * FROM order_product_list WHERE order_id = UNHEX(REPLACE(:orderId,'-',''))",
                Collections.singletonMap("orderId", orderId.toString()),
                orderProductRowMapper);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM order_product_list", Collections.emptyMap());
    }

    private Map<String, Object> toParamMap(OrderProduct orderProduct) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderProductId", orderProduct.getOrderProductId().toString());
        map.put("orderId", orderProduct.getOrderId().toString());
        map.put("productId", orderProduct.getProductId().toString());
        map.put("orderPrice", orderProduct.getOrderPrice());
        map.put("count", orderProduct.getCount());
        return map;
    }
}
