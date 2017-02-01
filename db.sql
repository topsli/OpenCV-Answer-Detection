-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 06, 2017 at 08:33 PM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 5.6.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db`
--

-- --------------------------------------------------------

--
-- Table structure for table `exam`
--

CREATE TABLE `exam` (
  `examid` int(8) NOT NULL,
  `examname` varchar(25) NOT NULL,
  `subjectid` int(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `exam`
--

INSERT INTO `exam` (`examid`, `examname`, `subjectid`) VALUES
(1, 'PreTest', 3),
(2, 'PostTest', 3);

-- --------------------------------------------------------

--
-- Table structure for table `schoolyear`
--

CREATE TABLE `schoolyear` (
  `IdSchoolYear` int(15) NOT NULL,
  `SchoolYear` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `schoolyear`
--

INSERT INTO `schoolyear` (`IdSchoolYear`, `SchoolYear`) VALUES
(1, 2016),
(2, 2015);

-- --------------------------------------------------------

--
-- Table structure for table `studentscore`
--

CREATE TABLE `studentscore` (
  `id` int(8) NOT NULL,
  `studentid` varchar(10) NOT NULL,
  `score` int(8) NOT NULL,
  `examid` int(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `studentscore`
--

INSERT INTO `studentscore` (`id`, `studentid`, `score`, `examid`) VALUES
(31, '21202864', 0, 1),
(32, '04358220', 80, 1),
(34, '97025727', 80, 1),
(35, '66513806', 40, 1),
(40, '21202864', 0, 2),
(41, '04358220', 40, 2),
(42, '97025727', 80, 2),
(43, '66513806', 40, 2);

-- --------------------------------------------------------

--
-- Table structure for table `subject`
--

CREATE TABLE `subject` (
  `IdSubject` int(15) NOT NULL,
  `subject` varchar(30) NOT NULL,
  `IdSchoolYear` int(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `subject`
--

INSERT INTO `subject` (`IdSubject`, `subject`, `IdSchoolYear`) VALUES
(3, 'Database', 1),
(4, 'Programming', 2),
(5, 'OpenCV', 1),
(6, 'Project', 2),
(7, 'SubjectTest', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `exam`
--
ALTER TABLE `exam`
  ADD PRIMARY KEY (`examid`),
  ADD KEY `subjectid` (`subjectid`);

--
-- Indexes for table `schoolyear`
--
ALTER TABLE `schoolyear`
  ADD PRIMARY KEY (`IdSchoolYear`);

--
-- Indexes for table `studentscore`
--
ALTER TABLE `studentscore`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`,`studentid`,`examid`),
  ADD UNIQUE KEY `studentid` (`studentid`,`examid`),
  ADD KEY `examid` (`examid`),
  ADD KEY `no_duplicate` (`studentid`,`examid`);

--
-- Indexes for table `subject`
--
ALTER TABLE `subject`
  ADD PRIMARY KEY (`IdSubject`),
  ADD KEY `IdSchoolYear` (`IdSchoolYear`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `exam`
--
ALTER TABLE `exam`
  MODIFY `examid` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `schoolyear`
--
ALTER TABLE `schoolyear`
  MODIFY `IdSchoolYear` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `studentscore`
--
ALTER TABLE `studentscore`
  MODIFY `id` int(8) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;
--
-- AUTO_INCREMENT for table `subject`
--
ALTER TABLE `subject`
  MODIFY `IdSubject` int(15) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `exam`
--
ALTER TABLE `exam`
  ADD CONSTRAINT `exam_ibfk_1` FOREIGN KEY (`subjectid`) REFERENCES `subject` (`IdSubject`);

--
-- Constraints for table `studentscore`
--
ALTER TABLE `studentscore`
  ADD CONSTRAINT `studentscore_ibfk_1` FOREIGN KEY (`examid`) REFERENCES `exam` (`examid`);

--
-- Constraints for table `subject`
--
ALTER TABLE `subject`
  ADD CONSTRAINT `subject_ibfk_1` FOREIGN KEY (`IdSchoolYear`) REFERENCES `schoolyear` (`IdSchoolYear`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
