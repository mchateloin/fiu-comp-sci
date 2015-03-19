(* Characterisitcs of F# )

  - Based on OCaml
  - Integrated with .NET platform
  - Functions are first class values that can be created and used as freely as any other kind of value.
  - Static scoping and eager evaluation, as in most modern languages.
  - Static typing via type inference: normally F# code need not include any type annotations, and the F# compiler automatically infers the best possible polymorphic type, allowing code reuse.
  - Pattern-matching syntax for function definitions by case analysis.
  - Rich module system.
  - Exception handling.
  - Automatic storage management via garbage collection.
  - Incremental compiler supporting interactive program development.
  - Succinct syntax that makes some keywords optional, and which pays attention to code indentation.
  - No automatic coercion among different types in expressions
(*)


(* Desirable characteristics of a programming language:)

  - expressiveness: Can the language directly represent a wide variety of computational objects?
  - implementability: Is it easy to implement the language efficiently, in both time and space?
  - predictability: Is it easy to reason about how programs in the language behave?
  - uniformity: Are the rules of what is allowed in the language simple and uniform, or are there exceptions? For example, can any type of value be returned by a function, or can only "simple" values be returned?
(*)


(* Why let is NOT akin to assignment)
The above is a global declaration of x, whose scope extends to the end of the session (unless a new declaration masks it).
The possibility of a new declaration of x is a bit confusing, since you might then think that let expressions are simply assignments to variables. But really let expressions are declarations of constants. For instance, if we now say
(*)


(* Some keywords)

in:

  Saying let ... in (...) makes it so that variables are bound in that expression. This is different then specifying a variable within a scope.
  Totally optional! You could just say let ... (...)
  More information: http://stackoverflow.com/questions/546287/meaning-of-keyword-in-in-f

  let z = 5 in (let z = 3*z+1 in 2*z) + z


Here the inner let expression defines z to be 16 in 2*z, so it has value 32. But once we leave the scope of the inner let, the value of z is still 5, so we get the final result 37.


fun:

  Used to declare an anonymous function. Although, it's pointless if you use it like this:

  let succ = fun n -> n+1

rec:

  Enables recursion in a function.

(*)

(* Comments )

  - (* Multi-line *)
  - // One-line
  - /// Visual Studio only

(*)


(* Indentation )

Identation matters because it's one way for the compiler to determine nesting/scope.

An illustration of most of the rules:

let printList list1 =
    for elem in list1 do
        if elem > 0 then
            printf "%d" elem
        elif elem = 0 then
            printf "Zero"
        else
            printf "(Negative number)"
        printf " "
    printfn "Done!"
printfn "Top-level context."
printList [-1;0;1;2;3]

Breaking along lines, like during function parameters, need indentation "farther than the closing construct"

let myFunction1 a b = a + b
let myFunction2(a, b) = a + b
let someFunction param1 param2 =
    let result = myFunction1 param1
                     param2
    result * 100
let someOtherFunction param1 param2 =
    let result = myFunction2(param1,
                     param2)
    result * 10

More information: http://msdn.microsoft.com/en-us/library/dd233191.aspx

(*)


(* Random)

Pattern matching tuple declaration:

  let (a,b,c) = (17, "bird", true)

Here (a,b,c) is a pattern that is matched against the tuple, binding identifiers a, b, and c. We use this all the time in function definitions:

  let rec power (m,n) = if n = 0 then 1.0 else m * power (m, n-1)

So since functions basically just take tuples, we can say they take exactly one input and do exactly one output every single time.

You get polymorphism when you do something like this:

  let swap (x,y) = (y,x)
  val swap : 'a * 'b -> 'b * 'a

Where swap can basically work for everything. The type variables are universally quantified, and its equivalent to saying this ugly phrase: (forall 'a, 'b)('a * 'b -> 'b * 'a)

But do something like this with numeric operators like +, *, /, -:

  > let double x = x+x;;
  val double : int -> int

And you'll get that int -> int function because '+' is overloaded for many numeric types, and fsharp has no way of knowing what you want, nor should incorrectly say it's polymorphic because of the '+'.

Type annotation to the rescue:

  > let double (x:string) = x+x;;
  val double : string -> string
  > let double x : string = x+x;;
  val double : string -> strin

Currying like a boss:

  > let cadd a = (fun b -> a+b);;
  val cadd : int -> (int -> int)

But that syntax sucks. This one is nicer:

 > let cadd a b = a+b;;
  val cadd : int -> int -> int

List.map applies a function to all elements of a list and returns the list of results.
List.filter: applies a predicate to each element of a list and returns the list of elements that satisfy the predicate.
List.reduce: uses a binary function to "reduce" a list down to a single value, associating to the

OH GOD NO NOT THE VALUE RESTRICTION:

  > List.rev [];;

    List.rev [];;
    ^^^^^^^^^^^

  stdin(48,1): error FS0030: Value restriction.
  The value 'it' has been inferred to have generic type
      val it : '_a list
  Either define 'it' as a simple data term, make it a function with explicit
  arguments or, if you do not intend for it to be generic, add a type annotation.

WHY? Function application can't work on polymorphic types.


(*)

// Convert infix operators to prefix functions with ():
(*)
//val it : (int -> int -> int) = <fun:it@47-10>
let triple = (*) 3;;
//val triple : (int -> int)
triple 7;;
//val it : int = 21

// :: (cons) new list with new element at front (new element can by ANYTHING, even another list)
"a" :: ["b"; "c"] = ["a"; "b"; "c"]
// Note that [1;2;3] = 1::[2;3] = 1::2::[3] = 1::2::3::[]
// So cons associates right!

// Appends (@) also associates right!
[5]@[1;2;3]
// Note appends is really like "concatenate two lists."

//Remember that arrow operator associates to the right!
let cadd a = (fun b -> a+b)

//output: val cadd : int -> int -> int
//Where it really means int -> (int -> int)


//But function application associates to the left!
let cadd a = (fun b -> a+b)
//val cadd : int -> int -> int
cadd 5 8
//val it : int = 13

//Say it out loud: ASSOCIATE ARROW LEFT! APPLY RIGHT! ASSOCIATE CONS RIGHT! ASSOCIATE APPENDS RIGHT!


//Prefix operators have higher precedence than infix operators
let succ n = n+1
succ 12    // val it : int = 13
succ 3*7   // val it : int = 28, because (3+1)*7
succ (3*7) // val it : int = 22, because (3*7)+1


// The Fucking Checklist for Recursion

///...


// The Wildcard
// Note that the wildcard '_' does not creating a binding
let third (_ :: _ :: x :: _) = x


// Whoa, nexted pattern matching
let foo x = function
  | 1 -> match x with
         | 0 -> true
         | 1 -> false
  | 2 -> true

// You can't use identifiers more than once in a pattern
(*
> let same = function
    | (n,n) -> true
    | _     -> false;;

    | (n,n) -> true
    -----^^

  stdin(100,6): error FS0038: 'n' is bound twice in this pattern
*)


// Define your aux functions on the INSIDE of your main function:
let map f xs =
  let rec map_aux = function
  | []    -> []
  | y::ys -> f y :: map_aux ys
  map_aux xs;;
// val map : ('a -> 'b) -> 'a list -> 'b list
// You get access to the main's parameters,

//Mutual recursion? Use simultaneous declaration with 'and'
let rec f n = if n = 0 then 1 else g n
and g m = m * f(m-1)
