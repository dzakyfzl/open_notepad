-- Pilih database (tanpa GO)
USE pacipapaDB;

-- Tabel Files
CREATE TABLE Files (
    fileID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    size BIGINT,
    path TEXT -- VARCHAR(MAX) diganti dengan TEXT
);

-- Tabel Accounts
CREATE TABLE Accounts (
    username VARCHAR(50) PRIMARY KEY,
    fileID INT,
    email VARCHAR(100),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    isAdmin BOOLEAN DEFAULT FALSE, -- BIT diganti BOOLEAN
    hashedPassword VARCHAR(255),
    salt VARCHAR(255),
    FOREIGN KEY (fileID) REFERENCES Files(fileID)
);

-- Tabel Notes
CREATE TABLE Notes (
    moduleID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    fileID INT,
    name VARCHAR(100),
    description TEXT, -- VARCHAR(MAX) diganti dengan TEXT
    course VARCHAR(100),
    major VARCHAR(100),
    dateUploaded DATETIME,
    visibility BOOLEAN DEFAULT TRUE, -- BIT diganti BOOLEAN
    FOREIGN KEY (username) REFERENCES Accounts(username),
    FOREIGN KEY (fileID) REFERENCES Files(fileID)
);

-- Tabel Ratings
CREATE TABLE Ratings (
    username VARCHAR(50),
    moduleID INT,
    dateRated DATETIME,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    PRIMARY KEY (username, moduleID),
    FOREIGN KEY (username) REFERENCES Accounts(username),
    FOREIGN KEY (moduleID) REFERENCES Notes(moduleID)
);

-- Tabel Bookmarks
CREATE TABLE Bookmarks (
    username VARCHAR(50),
    moduleID INT,
    dateBookmarked DATETIME,
    PRIMARY KEY (username, moduleID),
    FOREIGN KEY (username) REFERENCES Accounts(username),
    FOREIGN KEY (moduleID) REFERENCES Notes(moduleID)
);

-- Tabel Views
CREATE TABLE Views (
    viewID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    moduleID INT,
    dateViewed DATETIME,
    FOREIGN KEY (username) REFERENCES Accounts(username),
    FOREIGN KEY (moduleID) REFERENCES Notes(moduleID)
);

-- Tabel UserDetails
CREATE TABLE UserDetails (
    username VARCHAR(50) PRIMARY KEY,
    aboutMe TEXT, -- VARCHAR(MAX) diganti TEXT
    instagram VARCHAR(100),
    linkedin VARCHAR(100),
    FOREIGN KEY (username) REFERENCES Accounts(username)
);

-- Tabel Sessions
CREATE TABLE Sessions (
    sessionID VARCHAR(100) PRIMARY KEY,
    username VARCHAR(50),
    IPAddress VARCHAR(45),
    userAgent TEXT, -- VARCHAR(MAX) diganti TEXT
    dateCreated DATETIME,
    FOREIGN KEY (username) REFERENCES Accounts(username)
);
