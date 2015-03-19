// 1.

let rec gcd = function
  | (a,0) -> a
  | (a,b) -> gcd (b, a % b)

let (.*) (p1,q1) (p2, q2) =
  (  (p1*p2) / gcd ((p1*p2), (q1*q2))  , (q1*q2) / gcd ((p1*p2), (q1*q2))  )


let (.+) (p1,q1) (p2, q2) =
  (((p1*q2) + (p2*q1))/gcd (((p1*q2) + (p2*q1)), (q1*q2)), (q1*q2)/gcd (((p1*q2) + (p2*q1)), (q1*q2)))


// 2.
let revlists xs = List.map(fun x -> List.rev x) xs

// 3.
let rec interleave(lista, listb) =
  match (lista, listb) with
  | (lista, []) -> lista
  | ([], listb) -> listb
  | (lista, listb) -> [List.head lista;List.head listb] @ interleave(List.tail lista, List.tail listb)

// 4.
let rec gencut = function
  | (n, []) -> ([], [])
  | (1, head::tail) -> (head::[], tail)
  | (n, head::tail) -> let (left, right) = gencut(n-1, tail) in (head::left, right)

let cut l = gencut(List.length l / 2, l)

// 5.
let shuffle l = interleave ( cut l )

// 6.
let rec countaux(deck, target) =
  if deck = target then 0
  else 1 + countaux (shuffle deck, target)

let countshuffles n =
  match n with
  | 0 -> 1
  | n -> 1 + countaux(shuffle [1..n], [1..n])
  //The idea here is to shuffle to skip a shuffle for countaux so it doesn't just
  //return 0, and then you can add 1 to it.
