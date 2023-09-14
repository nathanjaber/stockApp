package desafio.nathan.StockApp.infrastructure.repositories;

import desafio.nathan.StockApp.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
