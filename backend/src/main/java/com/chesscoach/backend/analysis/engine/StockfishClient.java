package com.chesscoach.backend.analysis.engine;

import com.chesscoach.backend.analysis.dto.EvaluationResult;
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

    // function to write command to stockfish stdin (using writer)
    public void sendCommand(String command) throws IOException {
        writer.write(command + "\n");
        writer.flush();
    }

    // function to send command to get evaluation
    // synchronized as only one thread should execute this at a time or one user at a time(as multiple users can execute)
    public synchronized EvaluationResult evaluatePosition(String fen, int depth){
        try{
            sendCommand("position fen " + fen);
            sendCommand("go depth " + depth);

            Double centipawns = null;
            Integer mateInMoves = null;
            String bestMove = null;

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("info") && line.contains("score")) {
                    
                    // extract the cp from stockfish output
                    if (line.contains("score cp")) {
                        int cpIndex = line.indexOf("score cp") + 9;
                        String[] parts = line.substring(cpIndex).split(" ");
                        int cp = Integer.parseInt(parts[0]);
                        centipawns = cp / 100.0;
                        mateInMoves = null;

                    // extract mate in moves if any , set cp=0 then
                    } else if (line.contains("score mate")) {
                        int mateIndex = line.indexOf("score mate") + 11;
                        String[] parts = line.substring(mateIndex).split(" ");
                        mateInMoves = Integer.parseInt(parts[0]);
                        centipawns = null;
                    }
                }
                // extract the best move
                if (line.startsWith("bestmove")) {
                    String[] parts = line.split(" ");
                    if (parts.length > 1) {
                        bestMove = parts[1];
                    }
                    break;
                }
            }
            return new EvaluationResult(centipawns, mateInMoves, bestMove);
        }
        catch (IOException e){
            log.error("Error communicating with Stockfish during position evaluation", e);
            throw new RuntimeException("Stockfish evaluation failed", e);
        }
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