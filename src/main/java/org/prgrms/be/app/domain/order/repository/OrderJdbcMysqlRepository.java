package org.prgrms.be.app.domain.order.repository;

import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.order.Order;
import org.prgrms.be.app.domain.order.OrderStatus;
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
public class OrderJdbcMysqlRepository implements OrderRepository {

    private final static RowMapper<Order> orderRowMapper = (resultSet, i) -> mapToOrder(resultSet);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcMysqlRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Order mapToOrder(ResultSet resultSet) throws SQLException {
        UUID orderId = ConvertUtil.toUUID(resultSet.getBytes("order_id"));
        OrderStatus status = OrderStatus.of(resultSet.getString("status"));
        String address = resultSet.getString("address");
        String addressDetails = resultSet.getString("address_details");
        int zipcode = resultSet.getInt("zipcode");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        UUID memberId = ConvertUtil.toUUID(resultSet.getBytes("member_id"));

        return new Order(orderId, memberId, new Address(address, addressDetails, zipcode), createdAt, status);
    }

    @Override
    public Order insert(Order order) {
        int insertCnt = jdbcTemplate.update("INSERT INTO orders (order_id, status, address, address_details, zipcode, created_at, member_id) " +
                        "VALUES (UNHEX(REPLACE(:orderId,'-','')), :status, :address, :addressDetails, :zipcode, :createdAt, UNHEX(REPLACE(:memberId,'-','')))",
                toParamMap(order));

        if (insertCnt != 1) {
            throw new DatabaseUpdateException("order: insert가 수행되지 않았습니다.");
        }

        return order;
    }

    @Override
    public UUID delete(UUID orderId) {
        int deleteCnt = jdbcTemplate.update("DELETE FROM orders WHERE order_id = UNHEX(REPLACE(:orderId,'-',''))",
                Collections.singletonMap("orderId", orderId.toString()));

        if (deleteCnt != 1) {
            throw new DatabaseUpdateException("order: delete가 수행되지 않았습니다.");
        }

        return orderId;
    }

    @Override
    public Order update(Order order) {
        int updateCnt = jdbcTemplate.update("UPDATE orders SET status=:status, address=:address, address_details=:addressDetails, zipcode=:zipcode" +
                        " WHERE order_id = UNHEX(REPLACE(:orderId,'-',''))",
                toParamMap(order));

        if (updateCnt != 1) {
            throw new DatabaseUpdateException("order: update가 제대로 수행되지 않았습니다.");
        }

        return order;
    }

    @Override
    public List<Order> findByMemberId(UUID memberId) {
        return jdbcTemplate.query("SELECT * FROM orders WHERE member_id = UNHEX(REPLACE(:memberId,'-',''))",
                Collections.singletonMap("memberId", memberId.toString()),
                orderRowMapper);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM orders WHERE order_id = UNHEX(REPLACE(:orderId,'-',''))",
                    Collections.singletonMap("orderId", orderId.toString()),
                    orderRowMapper));
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseFindException("order: 해당 id를 가진 order가 존재하지 않습니다.", e);
        }
    }

    @Override
    public List<Order> getAllOrder() {
        return jdbcTemplate.query("SELECT * FROM orders", Collections.emptyMap(), orderRowMapper);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM orders", Collections.emptyMap());
    }

    private Map<String, Object> toParamMap(Order order) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", order.getId().toString());
        map.put("status", order.getOrderStatus().getStatus());
        map.put("address", order.getAddress().getAddress());
        map.put("addressDetails", order.getAddress().getDetails());
        map.put("zipcode", order.getAddress().getZipCode());
        map.put("createdAt", order.getCreatedAt());
        map.put("memberId", order.getMemberId().toString());

        return map;
    }
}
