package com.excel.hoster;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExcelRepository extends CrudRepository<ExcelFile, String> {
    ExcelFile findByFullTextId(String fullTextId);
}
