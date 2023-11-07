package com.reporte.demo.repositorios;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.reporte.demo.entidades.reportes;
public interface ReporteRepository extends PagingAndSortingRepository<reportes, Long> {
    
}
