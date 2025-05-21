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

CREATE TABLE Modules (
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
