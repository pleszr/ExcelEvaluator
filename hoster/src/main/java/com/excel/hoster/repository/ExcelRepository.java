package com.excel.hoster.repository;

import com.excel.hoster.repository.entity.ExcelFileEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ExcelRepository extends CrudRepository<ExcelFileEntity, String>{

    @Query(
            value = "select * from excel_file_entity where full_text_id = :fullTextId",
            nativeQuery = true)
    Optional<ExcelFileEntity> findExcelEntityByFullTextId(@Param("fullTextId") String fullTextId);

    @Transactional
    @Modifying
    @Query(
            value = "insert into excel_file_entity (definition_name, brick_name, attribute_name, full_text_id, version, file_name, excel_file) " +
                    "values (:definitionName, :brickName, :attributeName, :fullTextId, :version,:fileName, :excelFile) " +
                    "on duplicate key update definition_name = :definitionName, brick_name = :brickName, attribute_name = :attributeName, full_text_id = :fullTextId,version = :version, file_name = :fileName, excel_file = :excelFile",
            nativeQuery = true)
    void saveOrUpdateExcelFileEntity(
            @Param("definitionName") String definitionName,
            @Param("brickName") String brickName,
            @Param("attributeName") String attributeName,
            @Param("fullTextId") String fullTextId,
            @Param("version") String version,
            @Param("fileName") String fileName,
            @Param("excelFile") byte[] excelFile);

}
