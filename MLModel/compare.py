from evaluate import evaluate_model
import subprocess

#Retrieve previous evaluation results from model-versioning folder
f = open("../model-versioning/evaluate.txt", "r")
prev_eval = float(f.read())
f.close()

#Retrieve the evaluation result of the current model using the api
cur_eval = float(evaluate_model())


#Compare the results
# Git commit if current loss is smaller than previous loss
if cur_eval < prev_eval:
	f = open("../model-versioning/evaluate.txt", "w")
	f.write(cur_eval)
	f.close()
	subprocess.call(['./git_v1.sh'])	
else:
	subprocess.call(['./git_v2.sh'])	





