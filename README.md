# ♟️ Chess Coach

> An AI-powered interactive chess coaching platform that turns Stockfish engine analysis into a conversational, board-animated teaching experience.

---

## What is Chess Coach?

Chess Coach is **not another Chess.com clone**. It's an interactive chess *teacher* that bridges the gap between raw engine analysis and human understanding.

-  Upload any PGN game
-  Stockfish analyses every position on-demand as you navigate move by move
-  Ask the AI coach *"Why was my move bad?"* — get plain-English explanations grounded in engine truth
-  Watch the best variation animate on the board
-  Multi-turn conversation — follow up until you truly understand

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.x, Spring Security, JWT |
| Database | PostgreSQL + Flyway migrations |
| Chess Engine | Stockfish 16 (UCI via ProcessBuilder) |
| LLM | Ollama (deepseek-r1:7b) — server-side, no API key needed |
| PGN Parsing | bhlangonijr/chesslib |
| Frontend | React + Vite |
| Chessboard | react-chessboard + chess.js |
| State | Zustand |
| Deployment | Docker Compose, Render (backend), Vercel (frontend) |

---

## Architecture

```
User → React Frontend → Spring Boot API → Stockfish (subprocess)
                                        → Ollama LLM (local server)
                                        → PostgreSQL
```

**The Golden Rule**: Stockfish is the single source of chess truth. The LLM only interprets and explains — it never proposes moves or contradicts the engine.

---

## Local Development Setup

### Prerequisites
- Java 21+
- Node 20+
- Docker + Docker Compose
- Maven 3.9+

### Steps

```bash
# 1. Clone the repo
git clone https://github.com/Alan-John-Thomas/chess-coach.git
cd chess-coach

# 2. Set up environment
cp .env.example .env
# Fill in .env values

# 3. Start all services (DB + Backend + Frontend + Ollama)
docker compose -f docker-compose.dev.yml up

# Or run individually:
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm install && npm run dev
```

---


---

## License

MIT
