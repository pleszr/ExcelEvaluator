package com.excel.hoster;

import org.springframework.data.repository.CrudRepository;

public interface ExcelRepository extends CrudRepository<ExcelFile, String> {
}
