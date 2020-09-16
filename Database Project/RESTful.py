import json
from bson import json_util
from jsonschema import validate, ValidationError, SchemaError
import datetime
import sys
from bottle import run, request, abort, post, delete, put, get
from pymongo import MongoClient
import pprint
import uuid


client = MongoClient("mongodb+srv://jamieHorner:7xbFOIK92lSfWeUx@cluster0-juwne.mongodb.net/access?retryWrites=true&w=majority", ssl=True)
marketDb = client.market
accessDb = client.access
stocks = marketDb.stocks
credentials = accessDb.credentials
tokenList = []

'''
  Validate credentials request
  Params:
    body: JSON object
'''
def validate_credentials(body):
  schema = {
    "type": "object",
    "properties": {
      "username": {"type": "string"},
      "password": {"type": "string"},
    },
    "required": ["username", "password"],
    "additionalProperties": False,
  }
  try:
    validate(body, schema)
  except (SchemaError, ValidationError) as error:
    return error

  return None

'''
  Validate new stock record for insertion
  Params:
    body: JSON object
'''
def validate_new_record(body):
  schema = {
    "type": "object",
    "properties": {
      "ticker": {"type": "string"},
      "industry": {"type": "string"},
      "company": {"type": "string"},
      "performance (YTD)": {"type": "number"},
    },
    "required": ["ticker", "industry", "company", "performance (YTD)"],
  }
  try:
    validate(body, schema)
  except (SchemaError, ValidationError) as error:
    return error

  return None

'''
  Validate stock record update request
  Params:
    body: JSON object
'''
def validate_update_record(body):
  schema = {
    "type": "object",
    "properties": {
      "ticker": {"type": "string"},
      "field": {"type": "string"},
      "value": {"type": "string"},
    },
    "required": ["ticker", "field", "value"],
    "additionalProperties": False,
  }
  try:
    validate(body, schema)
  except (SchemaError, ValidationError) as error:
    return error

  return None

'''
  Validate report request
  Params:
    body: JSON object
'''
def validate_report_request(body):
  schema = {
    "type": "object",
    "properties": {
      "tickers": {
        "type": "array",
        "items": {"type": "string"},
        "minItems": 1,
      }
    },
    "required": ["tickers"],
    "additionalProperties": False,
  }
  try:
    validate(body, schema)
  except (SchemaError, ValidationError) as error:
    return error

  return None

'''
  Check credentials and return UUID token
  Body:
    username: String
    password: String
'''
@post('/login')
def check_login():
  body = json.load(request.body)
  validation = validate_credentials(body)
  if (validation != None):
    return str(validation) + "\n"

  username = body['username']
  password = body['password']

  try:
    result = credentials.find_one({"username" : username})
  except:
    return "Failed to validate credentials\n"

  if (result == None):
    return "Invalid Username or Password\n"
  
  if (result['password'] != password):
    return "Invalid Username or Password\n"

  newToken = uuid.uuid4()
  tokenList.append(str(newToken))
  return "Successful Login! Your user token is : " + str(newToken) + "\n"

'''
  Create a new set of credentials if username doesn't exist already
  Header:
    token: String
  Body:
    username: String
    password: String
'''
@post('/create_login')
def add_login():
  if (tokenList.count(request.headers.get('Token')) == 0):
    return "Invalid token\n"
  body = json.load(request.body)

  validation = validate_credentials(body)
  if (validation != None):
    return str(validation) + "\n"
  
  username = body['username']
  password = body['password']
    
  try:
    result = credentials.find_one({"username" : username})
  except:
    return "Failed to get credentials\n"

  if (result != None):
    return "Username exists already\n"

  try:
    result = credentials.insert_one({"username" : username, "password": password})
  except:
    return "Failed to insert credentials\n"

  return "Successfully addded credentials\n"

'''
  Create a new document in the stocks collection
  Header:
    token: String
  Body:
    ticker: String
    industry: String
    company: String
    performance (YTD): String
    ...additionalProperties
'''
@post('/create_record')
def add_DOC():
    if (tokenList.count(request.headers.get('Token')) == 0):
      return "Invalid token\n"
    body = json.load(request.body)

    validation = validate_new_record(body)
    if (validation != None):
      return str(validation) + "\n"

    try:
      stocks.insert_one(body)
    except:
      return "Record insert failure\n"
      
    return "Record inserted\n"
    
'''
  Gets all matching documents for ticker parameter in the stocks collection 
  Header:
    token: String
  Query Param:
    ticker: String
'''
@get('/read_records')
def read_DOC():
    if (tokenList.count(request.headers.get('Token')) == 0):
      return "Invalid token\n"
    name = request.query.ticker

    try:
      results = stocks.find({"ticker" : name})
    except NameError as error:
      return error

    results = [json.dumps(result, default=json_util.default, separators=(',', ':')) for result in results]
    return results

'''
  Update a document in the stocks collection based on its ticker value
  and the passed in field and value properties
  Header:
    token: String
  Body:
    ticker: String
    field: String
    value: String
'''
@put('/update_record')
def update_DOC():
    if (tokenList.count(request.headers.get('Token')) == 0):
      return "Invalid token\n"
    body = json.load(request.body)

    validation = validate_update_record(body)
    if (validation != None):
      return str(validation) + "\n"

    ticker =  body["ticker"]
    field =  body["field"]
    value =  body["value"]

    try:
      result = stocks.update_one({"ticker" : ticker},{"$set":{field : value}})  
    except NameError as error:
      return error
      
    return "Record: " + ticker + " updated\n" + "Field: " + field + " set to " + value + "\n"

'''
  Delete a document in the stocks collection based on the ticker query param
  Header:
    token: String
  Query Param:
    ticker: String
'''
@delete('/delete_record')
def delete_DOC():
    if (tokenList.count(request.headers.get('Token')) == 0):
      return "Invalid token\n"
    name = request.query.ticker
    try:
      result = stocks.delete_one({"ticker" : name})
    except NameError as error:
      return error
      
    return "Delete Successful\n"

'''
  Returns a list of the top 5 performing stocks YTD for documents
  found for the industry query param
  Header:
    token: String
  Query Param:
    industry: String
'''
@get('/portfolio')
def portfolio_DOC():
  if (tokenList.count(request.headers.get('Token')) == 0):
    return "Invalid token\n"
  industry = request.query.industry
  industry = industry.replace("_"," ")
  query = { "industry": industry }
  results = stocks.aggregate([
    {'$match': query},
    {"$group": {"_id": "$company"}},
    {"$project": {"_id": 0, "company": "$_id", "performance (YTD)": 1}},
    {"$sort": {"performance (YTD)": -1}},
    {"$limit": 5}
  ])
  results = [json.dumps(result, default=json_util.default, separators=(',', ':')) for result in results]
  return "Portfolio for "  + industry + "\n" + str(results) + "\n"

'''
  Create a report of documents found for each value in an array of ticker values
  Header:
    token: String
  Body:
    tickers: String[]
'''
@get('/report')
def report_DOC():
    if (tokenList.count(request.headers.get('Token')) == 0):
      return "Invalid token\n"
    body = json.load(request.body)

    validation = validate_report_request(body)
    if (validation != None):
      return str(validation) + "\n"

    names = body['tickers']
    
    results = []
    try:
      for name in names:
        results.append(stocks.find_one({"ticker" : name}))
        
    except NameError as error:
      return error

    results = [json.dumps(result, default=json_util.default, separators=(',', ':')) for result in results]
    return results

if __name__ == '__main__':
  run(host='localhost', port=4000)
