package br.com.fecaf.arduino.model;

import java.util.Date;

public record ExceptionResponse(Date date, String messege, String details) {
}
