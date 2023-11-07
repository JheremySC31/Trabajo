package com.reporte.demo.servicio;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.reporte.demo.entidades.reportes;
public interface reporteServicios {
    	public List<reportes> findAll();

        public Page<reportes> findAll(Pageable pageable);

	public void save(reportes reportes);

	public reportes findOne(Long id);

	public void delete(Long id);
}


