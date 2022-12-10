package org.prgrms.be.app.domain.brand.repository;

import org.prgrms.be.app.domain.Address;
import org.prgrms.be.app.domain.brand.Brand;
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
public class BrandJdbcMysqlRepository implements BrandRepository {

    private final static RowMapper<Brand> brandRowMapper = (resultSet, i) -> mapToBrand(resultSet);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BrandJdbcMysqlRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static Brand mapToBrand(ResultSet resultSet) throws SQLException {
        UUID brandId = ConvertUtil.toUUID(resultSet.getBytes("brand_id"));
        String name = resultSet.getString("name");
        String address = resultSet.getString("address");
        String addressDetails = resultSet.getString("address_details");
        int zipcode = resultSet.getInt("zipcode");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

        return new Brand(brandId, name, new Address(address, addressDetails, zipcode), createdAt);
    }

    @Override
    public Brand insert(Brand brand) {
        int insertCnt = jdbcTemplate.update("INSERT INTO brand (brand_id, name, address, address_details, zipcode, created_at)" +
                        " VALUES(UNHEX(REPLACE(:brandId,'-','')), :name, :address, :addressDetails, :zipcode, :createdAt)",
                toParamMap(brand));

        if (insertCnt != 1) {
            throw new DatabaseUpdateException("brand: insert가 수행되지 않았습니다.");
        }

        return brand;
    }

    @Override
    public UUID delete(UUID brandId) {
        int deleteCnt = jdbcTemplate.update("DELETE FROM brand WHERE brand_id = UNHEX(REPLACE(:brandId, '-', ''))",
                Collections.singletonMap("brandId", brandId.toString()));

        if (deleteCnt != 1) {
            throw new DatabaseUpdateException("brand: delete가 수행되지 않았습니다.");
        }

        return brandId;
    }

    @Override
    public Brand update(Brand brand) {
        int updateCnt = jdbcTemplate.update("UPDATE brand SET name=:name, address=:address, address_details=:addressDetails, zipcode=:zipcode WHERE brand_id=UNHEX(REPLACE(:brandId,'-',''))",
                toParamMap(brand));

        if (updateCnt != 1) {
            throw new DatabaseUpdateException("brand: update가 수행되지 않았습니다.");
        }

        return brand;
    }

    @Override
    public Optional<Brand> findById(UUID brandId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM brand WHERE brand_id = UNHEX(REPLACE(:brandId,'-',''))",
                    Collections.singletonMap("brandId", brandId.toString()),
                    brandRowMapper));
        } catch (EmptyResultDataAccessException e) {
            throw new DatabaseFindException("brand: 해당 id를 가진 brand가 존재하지 않습니다.", e);
        }
    }

    @Override
    public List<Brand> getAllBrand() {
        return jdbcTemplate.query("SELECT * FROM brand",
                Collections.emptyMap(),
                brandRowMapper);
    }

    @Override
    public List<Brand> findBrandsByName(String name) {
        return jdbcTemplate.query("SELECT * FROM brand WHERE name=:name",
                Collections.singletonMap("name", name),
                brandRowMapper);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM brand", Collections.emptyMap());
    }

    private Map<String, Object> toParamMap(Brand brand) {
        Map<String, Object> map = new HashMap<>();
        map.put("brandId", brand.getId().toString());
        map.put("name", brand.getName());
        map.put("address", brand.getAddress().getAddress());
        map.put("addressDetails", brand.getAddress().getDetails());
        map.put("zipcode", brand.getAddress().getZipCode());
        map.put("createdAt", brand.getCreatedAt());

        return map;
    }
}
