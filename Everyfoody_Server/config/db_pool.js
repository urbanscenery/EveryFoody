const mysql = require('mysql');
const dbConfig = {
    host: 'Host IP',
    port: 'port number', 
    user: 'user name',
    password: 'Connect password', 
    database: 'Schema name', 
    connectionLimit: 23 
};
const dbpool = mysql.createPool(dbConfig);

module.exports = dbpool;
