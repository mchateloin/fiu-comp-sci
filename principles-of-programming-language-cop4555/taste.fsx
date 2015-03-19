open System.Windows.Forms

let form = new Form(Visible=true, TopMost=true, Text="Welcome to F#")

let textB = new RichTextBox(Dock=DockStyle.Fill, Text="Here is some initial text")
form.Controls.Add(textB)

open System.IO
open System.Net

let http (url:string) =
  let req = System.Net.WebRequest.Create(url)
  let resp = req.GetResponse()
  let stream = resp.GetResponseStream()
  let reader = new StreamReader(stream)
  let html = reader.ReadToEnd()
  resp.Close()
  html

let geoff = http "http://www.cis.fiu.edu/~smithg"
textB.Text <- geoff
