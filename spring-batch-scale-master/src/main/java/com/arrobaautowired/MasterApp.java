package com.arrobaautowired;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal del Batch. Su misión sera
 * <ul>
 * <li>Clasificar los ficheros</li>
 * <li>Leer los ficheros</li>
 * <li>Repartir el trabajo entre los esclavos.</li>
 * <li>Gestionar el feedback de los esclavos.</li>
 * </ul>
 *
 * @author jamataran@gmail.com
 * @since 1.0.0
 */
@SpringBootApplication
@EnableBatchProcessing
public class MasterApp {
    public static void main(String[] args) {
        SpringApplication.run(MasterApp.class, args);
    }
}
