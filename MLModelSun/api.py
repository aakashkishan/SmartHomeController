from flask import Flask, request
from flask_restful import reqparse, abort, Api, Resource
from pred import pred_api
from train import train_api
from preprocess import preprocess_api
from evaluate import evaluate_model

app = Flask(__name__)
api = Api(app)

parser = reqparse.RequestParser()

# Pred
#   get the pred data by calling the prediction script
class Pred(Resource):
    def get(self):
        parser.add_argument('user_id', required=True, help="Please feed the user_id value")
        parser.add_argument('type', required=True, help="Please feed the type value")
        parser.add_argument('year', required=True, help="Please feed the year value")
        parser.add_argument('month', required=True, help="Please feed the month value")
        parser.add_argument('day', required=True, help="Please feed the day value")
        args = parser.parse_args()
        result = pred_api(args['user_id'], args['type'], args['year'], args['month'], args['day'])
        if not result:
            abort(404, message="Prediction failed")
        return result

# Train
#   preprocess the data and train the model 
class Train(Resource):
    def get(self):
        respond_string = 'Model Training Succeed'
        # preprocess
        # Todo: handle the error
        preprocess_api()
        # train the model
        result, message = train_api("./train/")
        if not result:
            abort(404, message)
        return respond_string

# Evaluate
#    evaluate the model and return the Mean Square Error
class Evaluate(Resource):
    def get(self):
        return evaluate_model()

##
## Actually setup the Api resource routing here
##
api.add_resource(Pred, '/pred')
api.add_resource(Train, '/train')
api.add_resource(Evaluate, '/eval')


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
