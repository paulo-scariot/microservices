import mongoose from 'mongoose';

import { MONGO_DB_URL, MONGO_USER, MONGO_PASS } from '../constants/secrets.js'

export function connectMongoDb() {
  mongoose.connect(MONGO_DB_URL, {
    user: MONGO_USER, 
    pass: MONGO_PASS,
    authSource: "admin"
  });
  mongoose.connection.on('connected', function(){
    console.log('The application connected to MongoDB sucessfully');
  });
  mongoose.connection.on('error', function(){
    console.error('Error when tried to connect to MongoDB');
  });
}