/* 
 * The MIT License
 *
 * Copyright 2016 Kolatat Thangkasemvathana.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
/**
 * Author:  Kolatat Thangkasemvathana
 * Created: 21-May-2016
 */

create table if not exists $bankTable (
    id integer not null primary key autoincrement,
    name varchar(255) not null,
    world varchar(63),
    x double not null,
    y double not null,
    z double not null,
    address varchar(511));

create table if not exists $marketTable (
    id integer not null primary key autoincrement,
    name varchar(255) not null,
    world varchar(63),
    x double not null,
    y double not null,
    z double not null,
    address varchar(511));

CREATE TABLE if not exists $transactionTable (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`account`	TEXT NOT NULL,
	`amount`	INTEGER NOT NULL,
	`type`	TEXT NOT NULL,
	`time`	NUMERIC NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`reference`	TEXT NOT NULL,
	FOREIGN KEY(`account`) REFERENCES ec_accounts(account)
);

CREATE TABLE "ec_accounts" (
	`account`	TEXT NOT NULL UNIQUE,
	`balance`	INTEGER NOT NULL,
	`updated`	NUMERIC NOT NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(account)
)