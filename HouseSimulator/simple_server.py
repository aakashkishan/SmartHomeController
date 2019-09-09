import socket
import traceback
import sys
import threading
import time

HEATER = "Heater"
CHILLER = "Chiller"

class HouseState(object):
   '''
   Model the house state
   '''
   def __init__(self):
      '''
      Initialize the house state
      '''
      self.__door = True
      self.__light = True
      self.__proximity = True
      self.__alarm_state = False
      self.__heater_state = True
      self.__chiller_state = False
      self.__dehumidifier = False
      self.__heater_state = False
      self.__chiller_state = False
      self.__temperature = 65
      self.__humidity = 90
      self.__hvac_mode = HEATER

      '''
      New house state variables
      '''
      self.__lock_state = False
      self.__registered = False
      self.__arriving = False
      self.__intruder = False
      self.__clear = False
      '''
      New house state variables end
      '''

      self.__param = ";"
      self.__end = "."

   def update_simulation(self):
      '''
      Update the house simulation. This is really very simple
      '''
      if self.__heater_state: self.__temperature += 1
      if self.__chiller_state: self.__temperature -= 1

      if self.__humidity<100 and self.__humidity>0:
         if self.__dehumidifier:
            self.__humidity -=1
         else:
            self.__humidity +=1

   def set_state(self, new_state):
      '''
      Handle set state requests
      '''
      params = new_state[3:-1].split(';')
      
      for par in params:
         if len(par) == 0: continue
         k,v = par.split('=')
         if k == "LS":
            if v == "1": self.__light = True
            else: self.__light = False
         elif k == "AS":
            if v == "1": self.__alarm_state = True
            else: self.__alarm_state = False
         elif k == "DS":
            if v == "1": self.__door = True
            else: self.__door = False
         elif k == "HUS":
            if v == "1": self.__dehumidifier = True
            else: self.__dehumidifier = False
         elif k == "PS":
            if v == "1": self.__proximity = True
            else: self.__proximity = False
         elif k == "HES":
            if v == "1": self.__heater_state = True
            else: self.__heater_state = False
         elif k == "CHS":
            if v == "1": self.__chiller_state = True
            else: self.__chiller_state = False
         elif k == "HM":
            if v == "1": self.__hvac_mode = HEATER
            else: self.__hvac_mode = CHILLER
         elif k == "DLS":
            if v == "1": self.__lock_state = True 
            else: self.__lock_state = False


   # Getters and setters for house properties
   def get_temperature(self): return self.__temperature

   def get_humidity(self): return self.__humidity

   def set_door(self, d): self.__door = d
   def get_door(self):
      if self.__door: return "1"
      return "0"

   def set_light(self, l): self.__light = l
   def get_light(self):
      if self.__light: return "1"
      return "0"

   def set_proximity(self, p):  self.__proximity = p
   def get_proximity(self):
      if self.__proximity: return "1"
      return "0"

   def set_alarm_state(self, a): self.__alarm_state = a
   def get_alarm_state(self):
      if self.__alarm_state: return "1"
      return "0"

   def set_heater_state(self, h): self.__heater_state = h
   def get_heater_state(self):
      if self.__heater_state: return "1"
      return "0"

   def set_chiller_state(self, h): self.__chiller_state = h
   def get_chiller_state(self):
      if self.__chiller_state: return "1"
      return "0"

   def set_hvac_mode(self, h): self.__hvac_mode = h
   def get_hvac_mode(self):
      if self.__hvac_mode == HEATER: return "1"
      return "0"

   def set_dehumidifier(self, h): self.__dehumidifier = h
   def get_dehumidifier(self):
      if self.__dehumidifier: return "1"
      return "0"

   def set_lock_state(self, d): self.__lock_state = d
   def get_lock_state(self):
      if self.__lock_state: return "1"
      return "0"

   def set_registered(self, d): self.__registered = d
   def get_registered(self):
      if self.__registered: return "1"
      return "0"

   def set_arriving(self, d): self.__arriving = d
   def get_arriving(self):
      if self.__arriving: return "1"
      return "0"

   def set_intruder(self, d): self.__intruder = d
   def get_intruder(self):
      if self.__intruder: return "1"
      return "0"

   def set_clear(self, d): self.__clear = d
   def get_clear(self):
      if self.__clear: return "1"
      return "0"

   def get_state(self):
      '''
      Handle get state requests
      '''
      return "TR={0};HR={1};DS={2};LS={3};PS={4};AS={5};HES={6};CHS={7};HM={8};HUS={9};DLS={10};RR={11};ARS={12};PIS={13};AC={14}".format(
                                                                                               self.get_temperature(),
                                                                                               self.get_humidity(),
                                                                                               self.get_door(),
                                                                                               self.get_light(),
                                                                                               self.get_proximity(),
                                                                                               self.get_alarm_state(),
                                                                                               self.get_heater_state(),
                                                                                               self.get_chiller_state(),
                                                                                               self.get_hvac_mode(),
                                                                                               self.get_dehumidifier(),
                                                                                               self.get_lock_state(),
                                                                                               self.get_registered(),
                                                                                               self.get_arriving(),
                                                                                               self.get_intruder(),
                                                                                               self.get_clear()
                                                                                               )
house = HouseState()

class UserThread(threading.Thread):
   '''
   Thread to mimic user behavior
   '''

   def __init__(self):
      '''
      Set up the stop event
      '''
      super().__init__()
      self.__stop = threading.Event()

   def stop(self):
      '''
      Terminate this thread
      '''
      self.__stop.set()

   def run(self):
      '''
      Run the user simulation thread
      '''
      while not self.__stop.is_set():

         print("Current state: {}".format(house.get_state()))
         cmd = input("Enter a command: d=[toggle door], l=[toggle light], p=[toggle proximity], RET=[show current status], dl=[toggle door lock], rr=[toggle register received], ars=[toggle arriving state], pis=[toggle intruder state], ac=[toggle all clear] :")

         if cmd == "d":
            if house.get_door() == "1": house.set_door(False)
            else: house.set_door(True)
            print ("door is now {}".format(house.get_door()))
         elif cmd == "l":
            if house.get_light() == "1": house.set_light(False)
            else: house.set_light(True)
            print ("light is now {}".format(house.get_light()))
         elif cmd == "p":
            if house.get_proximity() == "1": house.set_proximity(False)
            else: house.set_proximity(True)
            print ("proximity is now {}".format(house.get_proximity()))
         elif cmd == "dl":
            if house.get_lock_state() == "1": house.set_lock_state(False)
            else: house.set_lock_state(True)
            print ("Lock State is now {}".format(house.get_lock_state()))
         elif cmd == "rr":
            if house.get_registered() == "1": house.set_registered(False)
            else: house.set_registered(True)
            print ("Register received is now {}".format(house.get_registered()))
         elif cmd == "ars":
            if house.get_arriving() == "1": house.set_arriving(False)
            else: house.set_arriving(True)
            print ("Arriving is now {}".format(house.get_arriving()))
         elif cmd == "pis":
            if house.get_intruder() == "1": house.set_intruder(False)
            else: house.set_intruder(True)
            print ("Intruder is now {}".format(house.get_intruder()))
         elif cmd == "ac":
            if house.get_clear() == "1": house.set_clear(False)
            else: house.set_clear(True)
            print ("All clear is now {}".format(house.get_clear()))
         
         time.sleep(2)
         print("Current state: {}".format(house.get_state()))

      return

def main():
   '''
   Wait for incoming connections and run the simulation
   '''
   print('Starting House Simulator (Hub)')
   sys.stdout.flush()

   server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
   server_address = (sys.argv[1], int(sys.argv[2]))
   server.bind(server_address)
   server.listen(1)  # max backlog of connections

   while True:

      print ('Waiting for house connection on {0}:{1}'.format(server_address[0], server_address[1]))
      sys.stdout.flush()
      connection, client_address = server.accept()
      print("Connection from {0}".format(client_address))

#      user_thread = UserThread()
#      user_thread.start()

      try:
         while True:
            data = connection.recv(1024).decode('ascii')
            print(data)

            if data:
               if data[:2] == "GS":
                  su = "SU:{}.\n".format(house.get_state())
                  connection.sendall(su.encode())
                  print(su)

               elif data[:2] == "SS":
                  house.set_state(data)
                  connection.sendall("OK.\n".encode())
                  print("OK.\n")

               else:
                  print ("Error, unknown request: {}".format(data))

            else: break

            house.update_simulation()

      except Exception as e:
         print("Error: %s" % str(e))
         traceback.print_exc()
      finally:
         print("closing!")
         connection.close()
#         user_thread.stop()
   return

if __name__ == '__main__':
    main()