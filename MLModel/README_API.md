# ML Model pipeline APIs

 The current ML model pipeline API lives at `http://17654-foxtrot.isri.cmu.edu`. You can use the API for the following usage:

* train a model with the current live data
* get the prediction result of the current model
* evaluate the quality of the current model 



### Endpoints

* **GET**` http://17654-foxtrot.isri.cmu.edu:5001/train`

  * DESCRIPTION

    This endpoint returns a response message of whether the model training succeed.

  * RESPONSE CLASS (STATUS 200)

    A string of response message.

  * PARAMETERS

    No parameter is required.

  * EXAMPLES

    | URL Request                                    | Condition | Response Status Code | Response Content         |
    | ---------------------------------------------- | --------- | -------------------- | ------------------------ |
    | `http://17654-foxtrot.isri.cmu.edu:5001/train` | Normal    | 200                  | "Model Training Succeed" |
    | `http://17654-foxtrot.isri.cmu.edu:5001/train` | Exception | 400                  | "Can not read the data"  |

* **GET** `http://17654-foxtrot.isri.cmu.edu:5000/pred`

  * DESCRIPTION

    This endpoint returns the predicted time when the user would turn on the light.

  * RESPONSE CLASS (STATUS 200)

    A float number for the model prediction outcome.

  * PARAMETERS

    | Parameter   | Description                                                  | Data Type | Required? |
    | ----------- | ------------------------------------------------------------ | --------- | --------- |
    | **user_id** | ID of the user to predict. Example: '3440'                   | Int       | Yes       |
    | **type**    | Only accept "**day**" or "**night**". The type of the model to predict when to turn on the light in the morning or to turn off the light at night. | Enum      | Yes       |
    | **year**    | The year of the date to predict. Example: '2019'             | Int       | Yes       |
    | **month**   | The month of the date to predict. Example: '06'              | Int       | Yes       |
    | **day**     | The day of the date to predict. Example: '21'                | Int       | Yes       |

  * EXAMPLES

    | URL Request                                                  | Condition | Response Status Code | Response Content            |
    | ------------------------------------------------------------ | --------- | -------------------- | --------------------------- |
    | `http://17654-foxtrot.isri.cmu.edu:5000/pred?user_id=3440&type=day&year=2019&month=06&day=21` | Normal    | 200                  | "02:14"                     |
    | `http://17654-foxtrot.isri.cmu.edu:5000/pred?user_id=3440&type=day&year=2019&month=06` | Exception | 404                  | "Please feed the day value" |

* GET `http://17654-foxtrot.isri.cmu.edu:5001/eval`

  * DESCRIPTION

    This endpoint returns an evaluate value of whether the current model.

  * RESPONSE CLASS (STATUS 200)

    A float number of evaluation which is the Mean Square Error (MSE) of the ML model.

  * PARAMETERS

    No parameter is required.

  * EXAMPLES

    | URL Request                                   | Condition | Response Status Code | Response Content |
    | --------------------------------------------- | --------- | -------------------- | ---------------- |
    | `http://17654-foxtrot.isri.cmu.edu:5001/eval` | Normal    | 200                  | "142.3"          |