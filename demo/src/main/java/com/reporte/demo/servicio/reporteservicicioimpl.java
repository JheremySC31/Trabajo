package com.reporte.demo.servicio;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reporte.demo.entidades.reportes;
import com.reporte.demo.repositorios.ReporteRepository;

@Service
public class reporteservicicioimpl implements reporteServicios {
    

    @Autowired
	private ReporteRepository reporteRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<reportes> findAll() {
		return (List<reportes>) reporteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<reportes> findAll(Pageable pageable) {
		return reporteRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(reportes reportes) {
		reporteRepository.save(reportes);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		reporteRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public reportes findOne(Long id) {
		return reporteRepository.findById(id).orElse(null);
	}
	
}






	
