package com.msil.evaluation.repository;

import com.msil.evaluation.constants.QueryConstants;
import com.msil.evaluation.entity.Symbols;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepository extends JpaRepository<Symbols,Long> {
    @Query(QueryConstants.Symbol_Details_Query)
    Symbols findSymbolByPartialNameIgnoreCase(@Param("symbolName") String symbolName);
    boolean existsBySymbol(String symbol);

}
