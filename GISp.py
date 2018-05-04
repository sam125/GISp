import socket
import sys
import shapefile
import threading
import wx

from io import BytesIO as StringIO

#def save():
  #  try:
#        os.remove(r"C:\Users\sam_f\DecksC\Man Imag\Gsp\gsc\test.cpg")
#        os.remove(r"C:\Users\sam_f\DecksC\Man Imag\Gsp\gsc\test.shp")
#        os.remove(r"C:\Users\sam_f\DecksC\Man Imag\Gsp\gsc\test.shx")
#        os.remove(r"C:\Users\sam_f\DecksC\Man Imag\Gsp\gsc\test.sbx")
#        os.remove(r"C:\Users\sam_f\DecksC\Man Imag\Gsp\gsc\test.shp.xml")
#        os.remove(r"C:\Users\sam_f\DecksC\Man Imag\Gsp\gsc\test.dbf")
#        os.remove(r"C:\Users\sam_f\DecksC\Man Imag\Gsp\gsc\test.sbn")
#   except Exception:
#       print('Nao foi possivel guardar o ficheiro')
#      pass

stopper = True
exitFlag = 0
global trr
flag = True
global connection
global btn1
global btn2
global sock
global fdl

class RedirectText(object):
    def __init__(self, aWxTextCtrl):
        self.out = aWxTextCtrl

    def write(self, string):
        self.out.WriteText(string)



class myThread (threading.Thread):
    def __init__(self, threadID, name, counter, port, folder):
        print('Folder '+ folder)
        #print(name+"sdh")
        threading.Thread.__init__(self)
        self.name = name
        self.port = port
        self.folder = folder

    def shap(self, xx, y):


        nPoint=0
        nPoint +=1

        global folderr

        fold = self.folder
       # print(fold)
        if fold=="":
            #print("NULL name")
            wx.MessageBox('Insert the Directory File', 'Info', wx.OK | wx.ICON_INFORMATION)
        else:
          #  global fileDialog
           # pathname = fileDialog.GetPath()
            xx = float(xx)
            y = float(y)
            w = shapefile.Writer(shapefile.POINT)
            w.point(y, xx)
            w.field('FIRST_FLD')
            w.field('SECOND_FLD', 'C', '40')
            w.record('First', 'Point')
            # w.saveShp(name)
            # w.saveShx(name)
            # w.saveDbf(name)
            #print(fold)
            global fdl
            fdll=fdl.GetValue()
            w.save(fdll)
            print('Latitude: '+ str(xx))
            print('Longitude: '+ str(y))
            print('Point'+ +'saved')
            #subprocess.Popen("explorer"+fdll, shell=True)


    def run(self):
        global flag
        flag = True
        while (flag):
            self.opensave()


    def opensave(self):
        global flag

        #print('IP Adress: '+socket.gethostbyname(socket.gethostname()))
        # Create a TCP/IP socket
        global sock
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

   #     sock = socket.socket(socket.)
        # Bind the socket to the port
        server_address = ('', self.port)
        print('Starting up on %s port %s' % server_address)
        sock.bind(server_address)
        # Listen for incoming connections
        sock.listen(1)
        cood = []
        connect = True
        #while flag:
        print('Waiting for a connection')

        while flag == True:
            # Wait for a connection
            #print('oi')
            global connection


            try:
                connection, client_address = sock.accept()
                #print(connection)
                print('Connection from', client_address)
                # Receive the data in small chunks and retransmit it
                it = 0

                while connect == True and flag == True:
                    #print(stopper)
                    it += 1
                    data = connection.recv(20)
                    dt = data.decode('ascii')
                   # if float(dt) != 92222205555558:
                    #    print(dt)
                    if float(dt) == 92222205555558:
                        connection.sendall(data)
                        connect = False
                        flag=False

                        global btn1
                        btn1.Enable()
                        global btn2
                        btn2.Disable()

                    if float(dt) != 92222205555558:
                        cood.append(dt)
                    if len(cood) == 2:
                        self.shap(cood[0], cood[1])
                        # shap(cood[0], cood[1])
                        connection.sendall(data)
                        cood = []
                        # if data:
                        #   print (sys.stderr, 'sending data back to the client')
                        #  connection.sendall(data)
                        # else:
                        #   print (sys.stderr, 'no more data from', client_address)
                        #  break
            finally:
                try:

                    # Clean up the connection
                    print("Connection closed")
                    connection.close()
                    break
                except:
                    print("Connection closed")
                    break
                    pass







class server(wx.Frame):
    def __init__(self, parent, title):
        super(server, self).__init__(parent, title=title, size=(500, 400))
        self.InitUI()
        self.Centre()
        self.Show()
        icon = wx.EmptyIcon()
        icon.CopyFromBitmap(wx.Bitmap("icon.ico", wx.BITMAP_TYPE_ANY))
        self.SetIcon(icon)


    def startR(self, event, porta, folder):


        """
        This method is fired when its corresponding button is pressedwa
        """
        global trr
        try:
            port = int(porta.GetValue())
          #  print('Starting up on %s port %s' % port)
            if folder.GetValue() == "":
                print("NULL folder name")
                wx.MessageBox('Insert Directory', 'Info', wx.OK | wx.ICON_INFORMATION)
            else:
                global btn1
                btn1.Disable()
                global btn2
                btn2.Enable()

                port = int(porta.GetValue())
                thread1 = myThread(1, "nr_port", 1, port, folder.GetValue())
                thread1.daemon = True
                thread1.start()
                trr=thread1
                # thread1.exit()
                return thread1
        except:
            print("NULL port")
            wx.MessageBox('Insert port number', 'Info', wx.OK | wx.ICON_INFORMATION)

    def closing(self, event):
        global btn1
        global btn2
        global sock
        btn1.Enable()
        btn2.Disable()
        global flag
        global trr
        flag = False
        #print(trr.is_alive())
        sock.close()
      #  trr.exit()
        #print(trr.is_alive())
        #print(threading.enumerate())



    def getIp(self):

        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(('10.255.255.255', 1))
        IP=str(s.getsockname()[0])
        s.close()
        #print(IP)
        return IP

    def InitUI(self):
        try:
            IP=self.getIp()
            #IP=socket.gethostbyname(socket.gethostname())
        except:
            print('NULL IP adress')
            IP="NULL"
            pass

        panel = wx.Panel(self)
        vbox = wx.BoxSizer(wx.VERTICAL)
        hbox1 = wx.BoxSizer(wx.HORIZONTAL)
        st1 = wx.StaticText(panel,-1, label='IP Adress')#Label endereço IP
       # st3 = wx.StaticText(panel, label=IP, size=(100, 25))#Endereço IP
        hbox1.Add(st1, 0.5, wx.EXPAND | wx.ALIGN_LEFT | wx.WXK_RIGHT,5)
        self.t1 = wx.TextCtrl(panel, value=IP,style = wx.TE_READONLY|wx.TE_LEFT, size=(100,15))
       # hbox1.Add(st1, flag=wx.RIGHT, border=8)
       # hbox1.Add(st3, flag=wx.RIGHT, border=8)
        hbox1.Add(self.t1, 1, wx.EXPAND |wx.RIGHT,120)
        # vbox.Add(hbox1, flag=wx.EXPAND | wx.LEFT | wx.RIGHT | wx.TOP, border=10)
        st2 = wx.StaticText(panel, -1, label='Port', size=(25, 25))
        hbox1.Add(st2, flag=wx.LEFT, border=8)
        tc2 = wx.TextCtrl(panel, size=(10,25), style=wx.TE_PROCESS_ENTER)
       # self.Text_Enter = wx.TextCtrl(self, 2, style=wx.TE_PROCESS_ENTER, size=(125, 150), pos=(170, 0))
        hbox1.Add(tc2, wx.RIGHT , 1)
        vbox.Add(hbox1, flag=wx.EXPAND | wx.LEFT | wx.RIGHT | wx.TOP, border=10)

       # vbox.Add((-1, 5))
        hbox5 = wx.BoxSizer(wx.HORIZONTAL)

        self.folder = wx.TextCtrl(panel, style=wx.TE_PROCESS_ENTER | wx.TE_LEFT, size=(10, 2))
        hbox5.Add(self.folder, 1, wx.EXPAND | wx.RIGHT, 1)
        global fdl
        fdl=self.folder
        btnBrowse = wx.Button(panel, label="Browse...")

        hbox5.Add(btnBrowse, flag=wx.TOP | wx.RIGHT| wx.BOTTOM, border=-1.9)
        btnBrowse.Bind(wx.EVT_BUTTON, self.OnSaveAs)
        vbox.Add(hbox5, flag=wx.EXPAND | wx.LEFT | wx.RIGHT | wx.TOP, border=10)

        vbox.Add((-1, 10))
        hbox2 = wx.BoxSizer(wx.HORIZONTAL)
        st2 = wx.StaticText(panel, label='Tasks')

        hbox2.Add(st2)
        vbox.Add(hbox2, flag=wx.LEFT | wx.TOP, border=10)

        vbox.Add((-1, 10))

        hbox3 = wx.BoxSizer(wx.HORIZONTAL)
        tc3 = wx.TextCtrl(panel, wx.ID_ANY, size=(300, 100), style=wx.TE_MULTILINE | wx.TE_READONLY | wx.HSCROLL)

        hbox3.Add(tc3, proportion=1, flag=wx.EXPAND)
        vbox.Add(hbox3, proportion=1, flag=wx.LEFT | wx.RIGHT | wx.EXPAND, border=10)

        redir = RedirectText(tc3)
        sys.stdout = redir
        vbox.Add((-1, 25))
        #port=tc2.GetValue()

        #print(port)
        hbox4 = wx.BoxSizer(wx.HORIZONTAL)

        global btn1

        btn1 = wx.Button(panel, label='Start', size=(70, 30))#Start button
        cl=btn1.Bind( wx.EVT_BUTTON, lambda event: self.startR(event, tc2, self.folder ) )
        global btn2
        btn2 = wx.Button(panel, label='Stop', size=(70, 30))#Stop button
        hbox4.Add(btn2)
        btn2.Disable()
        btn2.Bind( wx.EVT_BUTTON, lambda event: self.closing(event))

        hbox4.Add(btn1)

        vbox.Add(hbox4, flag=wx.ALIGN_CENTER | wx.CENTER, border=10)
        vbox.Add((-1, 25))
        panel.SetSizer(vbox)

    def OnSaveAs(self, event):

        with wx.FileDialog(self, "Save shp, dbf, shx files", style=wx.FD_SAVE | wx.FD_OVERWRITE_PROMPT, pos=(10, 10)) as fileDialog:
            if fileDialog.ShowModal() == wx.ID_CANCEL:
                return  # the user changed their mind
            # save the current contents in the file
            pathname = fileDialog.GetPath()
            try:
                    self.folder.SetValue(pathname)
                    #self.doSaveData(file)
            except IOError:
                wx.LogError("Cannot save current data in file '%s'." % pathname)



    def ddirchoose(self, event):
        'Gives the user selected path. Use: dirchoose()'
        global _selectedDir, _userCancel  # you should define them before'
        dialog = wx.DirDialog(None, "Please choose your project directory:", style=1, pos=(10, 10))

        if dialog.ShowModal() == wx.ID_OK:
            _selectedDir = dialog.GetPath()
            self.folder.SetValue(_selectedDir)
            return _selectedDir
        else:
          dialog.Destroy()
          return _userCancel


if __name__ == '__main__':
    app = wx.App()
    server(None, title='GISp')
    app.MainLoop()
