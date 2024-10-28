# PCS

## Table of Contents

- [Project Overview](#project-overview)
- [Online address](#online-address)
- [Features](#features)
- [System Requirements](#system-requirements)
- [Installation Guide](#installation-guide)
- [Changelog](#changelog)

## Project Overview

The PCS is a community platform designed for plant enthusiasts to share knowledge, discuss gardening tips, and track plant care activities. It offers features like plant tracking, user profiles, and interactive forums to foster communication and engagement among plant lovers.

## Online address

A live address of the project is available <http://139.9.241.37:8837>.

## Features

- **User Profiles**: Each user can create and manage their profile, track personal plant care activities, and share their garden setup.
- **Plant Tracking**: Users can track the growth and care schedules for their plants.
- **Calendar Management**: Integrated calendar for tracking watering schedules, fertilization, and other care tasks.
- **Interactive Forums**: Users can participate in discussions, ask questions, and share tips.

## System Requirements

- **Database**: MySQL 8.0+
- **Backend**: Java17,SpringBoot3,Mybatis-plus,Sa-token
- **Frontend**: Vue.js 3 with TailwindCSS
- **Server**: Nginx

## Installation Guide

* Backend

1. Clone the repository:

   ```bash
   git clone https://github.com/zhuohans/IT-Project.git
   ```

2. Install the required dependencies:

   ```bash
   cd IT-Project/login
   mvn clean
   mvn package
   ```

   Repeat steps 2 and 3 under the Backend section for each module as needed (e.g., `remind/pcs-server`, `species/pcs-server`, etc.).

3. Run the application:

   ```bash
   java -jar pcs-server-0.0.1-SNAPSHOT.jar
   ```

* Frontend

1. Clone the repository:

   ```bash
   git clone https://github.com/zhuohans/IT-Project.git
   ```

2. Install the required dependencies:

   ```bash
   cd IT-Project/pcs-web-master
   pnpm install
   ```

3. Run the application:

   ```bash
   pnpm run dev
   ```

## Changelog

Check the [changelog](#) for recent updates and features added.

* 2024-09-26 Add user-defined reminders.
* 2024-10-06 Modify the homepage display method.
* 2024-10-06 Modify the page interaction method.
