package com.learning.springdbencryption.repo;

import com.learning.springdbencryption.entity.EncTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncTableRepo extends JpaRepository<EncTable, Long> {
}
