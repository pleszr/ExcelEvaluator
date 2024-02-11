package com.excel.hoster.repository;

import com.excel.hoster.repository.entity.ExcelFile;
import org.springframework.data.repository.CrudRepository;

public interface ExcelRepository extends CrudRepository<ExcelFile, String> {

}
