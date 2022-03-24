package pl.telech.tmoney.adm.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import pl.telech.tmoney.adm.model.entity.Setting;
import pl.telech.tmoney.commons.dao.interfaces.DAO;


public interface SettingDAO extends DAO<Setting>, JpaSpecificationExecutor<Setting> {

	Setting findByName(String name);
}
