CREATE TABLE games(
                      id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                      user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                      pgn TEXT NOT NULL,
                      white_player VARCHAR(100),
                      black_player VARCHAR(100),
                      event VARCHAR(255),
                      game_date DATE,
                      result VARCHAR(10),
                      uploaded_at TIMESTAMP DEFAULT  NOW()
)