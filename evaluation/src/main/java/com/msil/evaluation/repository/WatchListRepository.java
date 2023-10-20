package com.msil.evaluation.repository;


import com.msil.evaluation.entity.WatchList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepository extends JpaRepository<WatchList,Long> {

}
