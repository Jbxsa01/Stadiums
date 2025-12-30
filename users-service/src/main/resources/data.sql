-- Supprimer les données existantes
DELETE FROM users;

-- Réinitialiser la séquence (PostgreSQL)
-- Pour H2, cette ligne est ignorée car H2 supporte ALTER TABLE ... RESTART
ALTER SEQUENCE IF EXISTS users_id_seq RESTART WITH 5;

-- Insérer des utilisateurs de test
INSERT INTO users (id, nom, prenom, email, password, role) VALUES
                                                               (1, 'Dupont', 'Jean', 'admin@universite.fr', 'admin123', 'ADMIN'),
                                                               (2, 'Martin', 'Sophie', 'sophie.martin@doctorant.fr', 'pass123', 'DOCTORANT'),
                                                               (3, 'Bernard', 'Pierre', 'pierre.bernard@doctorant.fr', 'pass123', 'DOCTORANT'),
                                                               (4, 'Dubois', 'Marie', 'marie.dubois@directeur.fr', 'pass123', 'DIRECTEUR');
