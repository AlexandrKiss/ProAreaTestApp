package co.proarea.services.impl;

import co.proarea.models.DBFile;
import co.proarea.models.ProductUnit;
import co.proarea.repositories.DBFileRepository;
import co.proarea.services.StorageService;
import co.proarea.services.exception.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

	private final DBFileRepository dbFileRepository;

	@Autowired
	public StorageServiceImpl(DBFileRepository dbFileRepository) {
		this.dbFileRepository = dbFileRepository;
	}

	@Override
	@Transactional
	public DBFile store(MultipartFile file, ProductUnit productUnit, String servletPath) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + fileName);
			}
			if (fileName.contains("..")) {
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ fileName);
			}
			DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes(), productUnit);
			dbFileRepository.save(dbFile);
			dbFile.setFileLink(servletPath+dbFile.getId());
			log.info("IN store - DBFile: {} successfully added", dbFile);
			return dbFile;
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file " + fileName, e);
		}
	}

	@Override
	@Transactional
	public DBFile getFileObject(long fileID) {
		DBFile dbFile = dbFileRepository.findById(fileID).get();
		log.info("IN getFileObject - DBFile: {}", dbFile);
		return dbFile;
	}

	@Override
	@Transactional
	public List<DBFile> getFileObjects() {
		return dbFileRepository.findAll();
	}

	@Override
	@Transactional
	public ResponseEntity<Resource> getFile(long fileID) {
		DBFile dbFile = dbFileRepository.findById(fileID).get();
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(dbFile.getFileType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
				.body(new ByteArrayResource(dbFile.getData()));
	}

	@Override
	public boolean delete(long fileID) {
		dbFileRepository.deleteById(fileID);
		return true;
	}
}
