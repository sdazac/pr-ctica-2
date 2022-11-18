import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Main {
  
    static List<Empleado> empleados;

    public static void main(String[] args) throws IOException {
        cargarArchivo();
        rango();
        Coleccion();
        EmpXDep();
        NominaXDep();
        SalarioMayor();
        MayorSalario();
        MenorSalario();
        SalarioPromDep();
        SalarioPromedioGen();
        ValorTotalNom();     
    }
      
    public static void cargarArchivo() throws IOException{
        Pattern pattern=Pattern.compile(";");
        String fileName="empleado.csv";

        try(Stream<String> lines=Files.lines(Path.of(fileName))) {
            empleados = lines.map(line -> {
                String[] arr = pattern.split(line);
                return new Empleado(arr[0], arr[1], arr[2], Double.parseDouble(arr[3]), arr[4]);
            }).collect(Collectors.toList());
          
        }
    }

    static Function<Empleado, String> porPrimerNombre = Empleado::getPrimerNombre;
    static Function<Empleado, String> porApellidoPaterno = Empleado::getApellidoPaterno;

    static Comparator<Empleado> apellidoLuegoNombre=Comparator.comparing(porApellidoPaterno).thenComparing(porPrimerNombre);

    public static void ordApe(){
        
empleados.stream().sorted(apellidoLuegoNombre).forEach(System.out::println);
    }

    static Predicate<Empleado> ranguito=e-> (e.getSalario()>=3000 && e.getSalario()<=5500);

    public static void rango(){
        System.out.println("");
        System.out.println("Empleados con salario entre 3000 y 5500: \n");
        empleados.stream().filter(ranguito).sorted(Comparator.comparing(Empleado::getSalario)).forEach(empleado->System.out.println("Primer Nombre: " +empleado.getPrimerNombre()+ ", Apellido Paterno: " +empleado.getApellidoPaterno()+ ", Salario: " + empleado.getSalario()+", Departamento: " + empleado.getDepartamento() + "\n"));
}

    public static void Coleccion(){

      System.out.println("\n\nColección de empleados que pertenecen a cada departamento: \n\n");

        Map<String, List<Empleado>> Coleccion = empleados.stream().collect(Collectors.groupingBy(Empleado::getDepartamento));
        Coleccion.forEach((dep,emp)->{
                System.out.println("\n" + dep + "\n");
                
                
                emp.forEach(empleado->System.out.println("Primer Nombre: " + empleado.getPrimerNombre()+", Apellido Paterno: "+empleado.getApellidoPaterno()+", Salario: " + empleado.getSalario() + "\n"));
            }
        );
    }
        
    public static void EmpXDep(){

        System.out.println("\nCantidad de empleados por departamento: \n\n");

        Map<String, Long> EmpXDep = empleados.stream().collect(Collectors.groupingBy(Empleado::getDepartamento,TreeMap::new,Collectors.counting()));
        EmpXDep.forEach((dep,cont)->{
                System.out.println("El departamento de "+dep + " tiene " + cont+" empleados.\n");
            }
        );
    }

    public static void NominaXDep(){
      
        System.out.println("\n\nSumatoria de la  nómina por departamento: \n\n");
      
        Map<String, Double> NominaXDep = empleados.stream().collect(Collectors.groupingBy(Empleado::getDepartamento,TreeMap::new,Collectors.summingDouble(Empleado::getSalario)));
        NominaXDep.forEach((dep,sum)->{
                System.out.println("El departamento de "+dep + " tiene una nómina de " + sum+" pesos.\n");
            }
        );
    }

 
    public static void SalarioMayor(){
      
        System.out.println("\n\nNombre del empleado con mayor salario por departamento: \n\n");
      
        Map<String, Empleado> SalarioMayor=empleados.stream().collect(Collectors.groupingBy(Empleado::getDepartamento,TreeMap::new,Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Empleado::getSalario)), e->e.get())));
        SalarioMayor.forEach((dep,emp)->{              
          System.out.println(emp.getPrimerNombre()+" "+emp.getApellidoPaterno()+ " es el empleado más asalariado del departamento de: " + dep + ". Su salario es de " +emp.getSalario()+ "pesos.\n");
                }
        );
    }

    public static void MayorSalario(){
      System.out.println("\n\nNombre del empleado que mayor salario recibe (de todos): \n\n");
        Empleado salmax=empleados.stream().max(Comparator.comparingDouble(Empleado::getSalario)).get();
        System.out.println(salmax.getPrimerNombre()+ " " + salmax.getApellidoPaterno() + " es el empleado con mayor salario de toda la empresa. Su salario es de: " +salmax.getSalario()+ " pesos.");

    }

   
    public static void MenorSalario(){
      System.out.println("\n\nNombre del empleado que menor salario recibe (de todos): \n\n");
        Empleado salmin=empleados.stream().min(Comparator.comparingDouble(Empleado::getSalario)).get();
        System.out.println(salmin.getPrimerNombre()+ " " + salmin.getApellidoPaterno() + " es el empleado con menor salario de toda la empresa. Su salario es de: " +salmin.getSalario() + " pesos.");
    }

    public static void SalarioPromDep(){

        System.out.println("\n\nPromedio de salario por departamento: \n\n");

        Map<String,Double> SalarioPromDep=empleados.stream().collect(Collectors.groupingBy(Empleado::getDepartamento,TreeMap::new,Collectors.averagingDouble(Empleado::getSalario)));
        SalarioPromDep.forEach((dep,prom)->{
            System.out.println("El "+dep+" tiene como promedio de salarios "+prom+" pesos.\n");
        }
        );
    }
  
    public static void SalarioPromedioGen(){

        System.out.println("\n\nEl Salario Promedio General es de: "+empleados.stream().mapToDouble(Empleado::getSalario).average().getAsDouble()+" pesos.\n\n");
    }

    public static void ValorTotalNom(){
      
        System.out.println("\n\nEl valor total de la nómina: "+empleados.stream().mapToDouble(Empleado::getSalario).sum()+" pesos.\n\n");;
    }

}
