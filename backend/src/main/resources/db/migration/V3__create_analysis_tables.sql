CREATE TABLE position_cache(
    fen VARCHAR(150) PRIMARY KEY,
    evaluation DECIMAL(8,2),
    best_move VARCHAR(10),
    top_lines JSONB,
    classification VARCHAR(20),
    analysed_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE chat_sessions(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    game_id uuid NOT NULL REFERENCES games(id) ON DELETE CASCADE,
    user_id uuid NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE chat_messages(
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id uuid NOT NULL REFERENCES chat_sessions(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    move_context_fen VARCHAR(150),
    board_variation JSONB,
    created_at TIMESTAMP DEFAULT NOW()
);