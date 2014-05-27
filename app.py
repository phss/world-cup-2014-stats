from flask import Flask

app = Flask(__name__)

@app.route('/')
def hello():
  return "Hello world!"

@app.route('/players')
def players():
  with open('data/players_2010.json') as file:
    return file.read()
