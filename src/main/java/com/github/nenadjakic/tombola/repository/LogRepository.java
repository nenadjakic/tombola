package com.github.nenadjakic.tombola.repository;

import com.github.nenadjakic.tombola.model.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {

}
