package com.excel.hoster.repository;

import com.excel.hoster.repository.entity.ExcelFileEntity;
import org.springframework.data.repository.CrudRepository;

public interface ExcelRepository extends CrudRepository<ExcelFileEntity, String> {

}
