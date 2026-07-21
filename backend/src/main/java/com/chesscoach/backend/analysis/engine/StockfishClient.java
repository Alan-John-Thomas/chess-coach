package com.chesscoach.backend.analysis.engine;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class StockfishClient {

    @Value("${app.stockfish.path}")
    private String stockfishPath;

    private Process process;
    private BufferedReader reader;
    private BufferedWriter writer;

    // PostConstruct will run after bean creation when spring app runs
    @PostConstruct
    public void startEngine() {
        try {
            log.info("Starting Stockfish engine process from path: {}", stockfishPath);
            ProcessBuilder processBuilder = new ProcessBuilder(stockfishPath);
            this.process = processBuilder.start();
            // reader reads from process.getInputStream which is stockfish stdout
            this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // writer writes to process.getOutputStream which is stockfish stdin
            this.writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            // Send UCI initialization command
            // send command fn is defined below
            sendCommand("uci");

            // Read engine output until it signals it is ready (uciok)
            String line;
            while ((line = reader.readLine()) != null) {
                if ("uciok".equals(line)) {
                    log.info("Stockfish engine initialized successfully (received uciok)");
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Failed to start Stockfish engine at path: {}", stockfishPath, e);
            throw new RuntimeException("Could not start Stockfish engine", e);
        }
    }

    public void sendCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
    }

    // runs before the spring app closes
    @PreDestroy
    public void stopEngine() {
        if (process != null && process.isAlive()) {
            try {
                sendCommand("quit");
            } catch (IOException e) {
                log.warn("Error sending quit command to Stockfish", e);
            } finally {
                process.destroy();
                log.info("Stockfish engine process terminated.");
            }
        }
    }
}