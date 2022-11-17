package com.gridsum.utils;

import com.gridsum.domain.GpsCountryEntity;
import com.gridsum.domain.GpsEntity;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.gridsum.controller.GpsUpdate.TAG_CHARACTER;

/**
 * @author xulei
 * @date 2022/9/1
 * @description: 测试
 */
@Slf4j
@UtilityClass
public class FileUtil {

    public <T> List<T>
    loadCSVFile(File file,
                CSVFormat format,
                Function<CSVRecord, T> parser) {
        List<T> results = new ArrayList<>();
        try (Reader in = new FileReader(file)) {
            Iterable<CSVRecord> records = format.parse(in);
            records.forEach(record -> {
                results.add(parser.apply(record));
            });
        } catch (Exception e) {
            log.error("read data from {} exception", file.getName(), e);
//            throw new RepositoryException(e.getMessage());
        }
        return results;
    }

    public static List<GpsCountryEntity> readGpsFile(String path) {
        CSVFormat format = CSVFormat.Builder.create()
                .setSkipHeaderRecord(true)
                .setDelimiter(TAG_CHARACTER)
                .setAllowDuplicateHeaderNames(false)
                .setTrim(true)
                .build();

        Function<CSVRecord, GpsCountryEntity> caaIpDBParser =
                (record) -> GpsCountryEntity.builder()
                        .country(record.get(0))
                        .province(record.get(1))
                        .city(record.get(3))
                        .polyline(record.get(6))
                        .build();

        return FileUtil.loadCSVFile(new File(path), format, caaIpDBParser);
    }
}
