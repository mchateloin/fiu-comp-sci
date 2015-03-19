let inner = fun left -> fun right ->
  let rec auxinner = function
  | ([],[]) -> 0
  | ([],_) -> failwith "Lists must have matching length."
  | (_,[]) -> failwith "Lists must have matching length."
  | (u::us, v::vs) -> (u*v) + product(us, vs)
  auxinner (left, right)

let multiply = fun (left, right) ->

  let inner = fun left -> fun right ->
    let rec auxinner = function
    | ([],[]) -> 0
    | ([],_) -> failwith "Lists must have matching length."
    | (_,[]) -> failwith "Lists must have matching length."
    | (u::us, v::vs) -> (u*v) + product(us, vs)
    auxinner (left, right)

  let rec transpose = function
  | [] -> failwith "Cannot transpose a 0 by z matrix"
  | []::xs -> []
  | xs -> List.map List.head xs::transpose (List.map List.tail xs)

  let righttransposed = transpose right

  let rec auxmultiply = function
  | ([], []) -> failwith "Cannnot multiply with 0 by z matrix"
  | (_, []) -> failwith "Cannnot multiply with 0 by z matrix"
  | ([], vs) -> []
  | (urow::us, vs) -> (List.map (fun row -> inner urow row) vs)::auxmultiply(us,vs)

  auxmultiply(left, righttransposed)

let flatten1 xs = List.fold (@) [] xs
let flatten2 xs = List.foldBack (@) xs []

let rec fold f a = function
| []    -> a
| x::xs -> fold f (f a x) xs;;

let rec foldBack f xs a =
match xs with
| []    -> a
| y::ys -> f y (foldBack f ys a);;


type 'a stream = Cons of 'a * (unit -> 'a stream)

let rec upfrom n = Cons(n, fun () -> upfrom(n+1))

let rec take n (Cons(x, xsf)) =
      if n = 0 then []
               else x :: take (n-1) (xsf());;
let rec filter p (Cons(x, xsf)) =
      if p x then Cons(x, fun () -> filter p (xsf()))
             else filter p (xsf());;

let rec map f (Cons(x, xsf)) = Cons(f x, fun () -> map f (xsf()))

type Exp =
    Num of int
  | Neg of Exp
  | Sum of Exp * Exp
  | Diff of Exp * Exp
  | Prod of Exp * Exp
  | Quot of Exp * Exp

let rec evaluate = function
  | Num n -> (Some n)
  | Neg e ->
    match evaluate e with
    | Some n -> Some(-1*n)
    | _ -> None
  | Sum(e1, e2) ->
    match (evaluate e1, evaluate e2) with
    | (Some m, Some n) -> Some(m+n)
    | (_, _) -> None
  | Diff(e1, e2) ->
    match (evaluate e1, evaluate e2) with
    | (Some m, Some n) -> Some(m-n)
    | (_, _) -> None
  | Prod(e1, e2) ->
    match (evaluate e1, evaluate e2) with
    | (Some m, Some n) -> Some(m*n)
    | (_, _) -> None
  | Quot(e1, e2) ->
    match (evaluate e1, evaluate e2) with
    | (Some m, Some 0) -> None
    | (Some m, Some n) -> Some(m/n)
    | (_, _) -> None
