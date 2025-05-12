CREATE DATABASE OpenNotepad
    DEFAULT CHARACTER SET = 'utf8mb4';

USE OpenNotepad;

CREATE TABLE Accounts (
    username VARCHAR(50) PRIMARY KEY,
    fileID INT,
    email VARCHAR(100),
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    isAdmin BOOLEAN DEFAULT FALSE,
    hashedPassword VARCHAR(255),
    salt VARCHAR(255),
    FOREIGN KEY (fileID) REFERENCES Files(fileID)
);

CREATE TABLE Files (
    fileID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    type VARCHAR(50),
    size INT,
    path TEXT
);

CREATE TABLE Modules (
    moduleID INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    fileID INT,
    name VARCHAR(100),
    description TEXT,
    course VARCHAR(100),
    major VARCHAR(100),
    dateUploaded DATETIME,
    visibility BOOLEAN DEFAULT TRUE,
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
    aboutMe TEXT,
    instagram VARCHAR(100),
    linkedin VARCHAR(100),
    FOREIGN KEY (username) REFERENCES Accounts(username)
);

CREATE TABLE Sessions (
    sessionID VARCHAR(100) PRIMARY KEY,
    username VARCHAR(50),
    IPAddress VARCHAR(45),
    userAgent TEXT,
    dateCreated DATETIME,
    FOREIGN KEY (username) REFERENCES Accounts(username)
);


