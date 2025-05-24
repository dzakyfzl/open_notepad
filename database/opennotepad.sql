-- Active: 1747461409837@@pacipapa.database.windows.net@1433@pacipapaDB

USE pacipapaDB;
GO

CREATE TABLE Files (
    fileID INT PRIMARY KEY IDENTITY(1,1),
    name VARCHAR(100),
    type VARCHAR(50),
    size BIGINT,
    path VARCHAR(MAX)
);

CREATE TABLE Accounts (
    username VARCHAR(50) PRIMARY KEY,
    fileID INT,
    email VARCHAR(100),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    isAdmin BIT DEFAULT 0,
    hashedPassword VARCHAR(255),
    salt VARCHAR(255),
    FOREIGN KEY (fileID) REFERENCES Files(fileID)
);

CREATE TABLE Notes (
    moduleID INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50),
    fileID INT,
    name VARCHAR(100),
    description VARCHAR(MAX),
    course VARCHAR(100),
    major VARCHAR(100),
    dateUploaded DATETIME,
    visibility BIT DEFAULT 1,
    FOREIGN KEY (username) REFERENCES Accounts(username),
    FOREIGN KEY (fileID) REFERENCES Files(fileID)
);

CREATE TABLE Ratings (
    username VARCHAR(50),
    moduleID INT,
    dateRated DATETIME,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    PRIMARY KEY (username, moduleID),
    FOREIGN KEY (username) REFERENCES Accounts(username),
    FOREIGN KEY (moduleID) REFERENCES Modules(moduleID)
);

CREATE TABLE Bookmarks (
    username VARCHAR(50),
    moduleID INT,
    dateBookmarked DATETIME,
    PRIMARY KEY (username, moduleID),
    FOREIGN KEY (username) REFERENCES Accounts(username),
    FOREIGN KEY (moduleID) REFERENCES Modules(moduleID)
);

CREATE TABLE Views (
    username VARCHAR(50),
    moduleID INT,
    dateViewed DATETIME,
    PRIMARY KEY (username, moduleID),
    FOREIGN KEY (username) REFERENCES Accounts(username),
    FOREIGN KEY (moduleID) REFERENCES Modules(moduleID)
);

CREATE TABLE UserDetails (
    username VARCHAR(50) PRIMARY KEY,
    aboutMe VARCHAR(MAX),
    instagram VARCHAR(100),
    linkedin VARCHAR(100),
    FOREIGN KEY (username) REFERENCES Accounts(username)
);

CREATE TABLE Sessions (
    sessionID VARCHAR(100) PRIMARY KEY,
    username VARCHAR(50),
    IPAddress VARCHAR(45),
    userAgent VARCHAR(MAX),
    dateCreated DATETIME,
    FOREIGN KEY (username) REFERENCES Accounts(username)
);

-- 1. Insert data dummy ke tabel Files (diperlukan untuk relasi foreign key)
INSERT INTO Files (name, type, size, path) VALUES
('note1.pdf', 'application/pdf', 1024, '/uploads/note1.pdf'),
('note2.pdf', 'application/pdf', 2048, '/uploads/note2.pdf'),
('note3.pdf', 'application/pdf', 3072, '/uploads/note3.pdf');

-- 2. Insert data dummy ke tabel Accounts (diperlukan untuk relasi foreign key)
INSERT INTO Accounts (username, email, firstName, lastName, hashedPassword, salt, isAdmin) VALUES
('john_doe', 'john@example.com', 'John', 'Doe', 'hashed123', 'salt123', 0),
('jane_smith', 'jane@example.com', 'Jane', 'Smith', 'hashed456', 'salt456', 0),
('bob_brown', 'bob@example.com', 'Bob', 'Brown', 'hashed789', 'salt789', 0);

-- 3. Insert data dummy ke tabel Notes (data utama untuk diambil API)
INSERT INTO Notes (username, fileID, name, description, course, major, dateUploaded, visibility) VALUES
('john_doe', 1, 'Pemrograman Dasar', 'Pengenalan algoritma dan pemrograman', 'CS101', 'Computer Science', GETDATE(), 1),
('jane_smith', 2, 'Struktur Data', 'Array, Linked List, dan Tree', 'CS201', 'Computer Science', GETDATE(), 1),
('bob_brown', 3, 'Basis Data', 'Konsep database dan SQL', 'CS301', 'Information Systems', GETDATE(), 1),
('john_doe', 1, 'Kalkulus', 'Limit, turunan, dan integral', 'MATH101', 'Mathematics', GETDATE(), 1),
('jane_smith', 2, 'Fisika Dasar', 'Mekanika klasik', 'PHY101', 'Physics', GETDATE(), 0); -- Note tidak visible

