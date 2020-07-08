package com.incomm.scheduler.dao.impl;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.incomm.scheduler.dao.PartnerDAO;
import com.incomm.scheduler.model.PartnerProductInfo;

@Component
public class PartnerDAOImpl extends JdbcDaoSupport implements PartnerDAO {

	private static final String PARTNER_SELECT_QUERY = "select part.partner_id, part.partner_name, part.mdm_id,  prod.product_id, "
			+ "prod.product_name,pp.attributes, pp.is_default, pur.purse_id, pur.ext_purse_id, pur.purse_type_id  "
			+ "(select NVL(attribute_value,0) from attribute_definition where attribute_group='Global Parameters' and attribute_name='topupPreemptNumberOfDays')preemptDays "
			+ "from partner part, product prod, "
			+ "product_purse pp, purse pur "
			+ "where pur.purse_id = pp.purse_id and pp.product_id = prod.product_id and prod.partner_id = part.partner_id "
			+ "and part.partner_id = :partnerId and  prod.product_id = :productId  and upper(pur.ext_purse_id) = upper(:purseName)";

	private static final String PRODUCT_PURSE_SELECT_QUERY = "select part.partner_id, part.partner_name, part.mdm_id,  prod.product_id, "
			+ "prod.product_name,pp.attributes, pp.is_default, pur.purse_id, pur.ext_purse_id, pur.purse_type_id, "
			+ "(select NVL(attribute_value,0) from attribute_definition where attribute_group='Global Parameters' and attribute_name='topupPreemptNumberOfDays')preemptDays "
			+ "from partner part, product prod, "
			+ "product_purse pp, purse pur "
			+ "where pur.purse_id = pp.purse_id and pp.product_id = prod.product_id and prod.partner_id = part.partner_id "
			+ "and pp.is_default !='Y' and (NVL(pp.attributes.Purse.autoTopupEnable,'Disable')='Enable' OR NVL(pp.attributes.Purse.autoReloadEnable,'Disable')='Enable' "
			+ "OR NVL(pp.attributes.Purse.rolloverEnable,'Disable')='Enable')";

	private static final String UPDATE_PURSE_ATTRIBUTES = "update product_purse set attributes= :attributes where product_id = :productId and purse_id= :purseId";
	private RowMapper<PartnerProductInfo> partnerInfoRowMapper = this.getPartnerInfoRowMapper();

	@Autowired
	public void setDs(@Qualifier("configurationDs") DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public Optional<PartnerProductInfo> getPartnerProductInfo(long partnerId, long productId, String purseName) {

		List<PartnerProductInfo> partnerInfos = getJdbcTemplate().query(PARTNER_SELECT_QUERY, partnerInfoRowMapper, partnerId, productId,
				purseName);

		return CollectionUtils.isEmpty(partnerInfos) ? Optional.empty() : Optional.of(partnerInfos.get(0));
	}
	
	@Override
	public List<PartnerProductInfo> getProductPurseInfo() {

		return getJdbcTemplate().query(PRODUCT_PURSE_SELECT_QUERY, partnerInfoRowMapper);

	}

	private RowMapper<PartnerProductInfo> getPartnerInfoRowMapper() {
		return (rs, rowNum) -> PartnerProductInfo.builder()
				.partnerId(rs.getLong("partner_id"))
				.productId(rs.getLong("product_id"))
				.purseId(rs.getLong("purse_id"))
				.purseName(rs.getString("ext_purse_id"))
				.mdmId(rs.getString("mdm_id"))
				.isDefault("Y".equals(rs.getString("is_default")))
				.partnerName(rs.getString("partner_name"))
				.productName(rs.getString("product_Name"))
				.purseTypeId(rs.getInt("purse_type_id"))
				.productAttributes(rs.getString("attributes"))
				.preemptDays(rs.getInt("preemptDays"))
				.build();
	}
	
	@Override
	public void updatePurseAttributes(String attributes,long productId,long purseId){
		
		getJdbcTemplate().update(UPDATE_PURSE_ATTRIBUTES, attributes, productId, purseId);
		
	}

}
