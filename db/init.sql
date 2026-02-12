CREATE TABLE IF NOT EXISTS issues (
  id SERIAL PRIMARY KEY,
  category TEXT NOT NULL,
  severity INT NOT NULL,
  description TEXT NOT NULL,
  lat DOUBLE PRECISION NOT NULL,
  lng DOUBLE PRECISION NOT NULL,
  created_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO issues (category, severity, description, lat, lng)
VALUES
('Pothole', 3, 'Demo pothole near downtown', 29.9511, -90.0715),
('Streetlight', 2, 'Light flickering at night', 29.9530, -90.0732);
