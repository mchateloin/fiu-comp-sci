// Skeleton file for PCF interpreter

// This sets F# to read from whatever directory contains this source file.
System.Environment.set_CurrentDirectory __SOURCE_DIRECTORY__;;

#load "parser.fsx"

// This lets us refer to "Parser.Parse.parsefile" simply as "parsefile",
// and to constructors like "Parser.Parse.APP" simply as "APP".
open Parser.Parse

let rec subst e x t =
  match e with
  | ID nval  -> if nval = x then t else ID nval
  | NUM nval  -> NUM nval
  | BOOL bval -> BOOL bval
  | ISZERO -> ISZERO
  | PRED   -> PRED
  | SUCC   -> SUCC
  | IF (bval, e1, e2) -> // ...
  | APP (e1, e2)  -> // ...
  | FUN (s, e) -> if x = s then FUN (s, e) else FUN (s, subst e x t)
  | REC (s, e) -> if x = s then REC (s, e) else REC (s, subst e x t)
  | _ -> ERROR "Invalid substitution"

let rec interp = function

  | (ERROR s, _, _) -> ERROR s
  | (_, ERROR s, _) -> ERROR s
  | (_, _, ERROR s) -> ERROR s
  | ID s          -> ID s

  //1.
  | NUM n -> NUM n

  //2. true => true and false => false
  | BOOL b -> BOOL b

  //3. succ => succ, pred => pred, and iszero => iszero.
  | ISZERO -> ISZERO
  | PRED -> PRED
  | SUCC -> SUCC

  // Everything else?
  | IF (e1, e2, e3) ->
      match (interp e1, e2, e3) with
      | (ERROR s, _, _) -> ERROR s
      | (_, ERROR s, _) -> ERROR s
      | (_, _, ERROR s) -> ERROR s
      | (BOOL b, eLeft, eRight) ->
          match b with
          | true      -> interp eLeft
          | false     -> interp eRight
      | (e1, eLeft, eRight)  -> ERROR "Conditional statements must be boolean."

  | APP(_, ERROR s) -> ERROR "Values are incorrect."
  | APP(ERROR s, _) -> ERROR "Values are incorrect."
  | APP (e1, e2) ->
      match (interp e1, interp e2) with

      | (ERROR s, _) -> ERROR s
      | (_, ERROR s) -> ERROR s

      | (SUCC, NUM n) -> NUM (n + 1)
      | (SUCC, v) -> ERROR (sprintf "Only values of type INT can be incremented.")

      | (PRED, NUM n) ->
      | (PRED, v) -> ERROR "Only values of type INT can be decremented."


      | (ISZERO, NUM 0) -> BOOL true
      | (ISZERO, NUM n) -> BOOL false
      | (ISZERO, v) -> ERROR "The function 'ISZERO' only tales values of 'INT'."

      | (FUN (x, e), eX) -> interp (subst e x eX )
      | (REC (x, f), e) -> interp (APP ( subst f x (REC (x, f)), e))
      | (s1, s2) -> APP (s1, s2)

  //9. (fun x -> e) => (fun x -> e)
  | FUN (x, e) -> FUN (x, e) //Rule (9)


  //11.
  (*
          e[x:=(rec x -> e)] => v
         --------------------------
            (rec x -> e) => v
  *)
  | REC (x, e) -> REC (x, e) //Rule (11)


// Here are two convenient abbreviations for using your interpreter.
let interpfile filename = filename |> parsefile |> interp

let interpstr sourcecode = sourcecode |> parsestr |> interp
