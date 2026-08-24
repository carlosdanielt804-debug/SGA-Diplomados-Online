import os

# ==========================================
# JERARQUÍA DE PROGRAMAS ACADÉMICOS (POLIMORFISMO)
# ==========================================
class ProgramaAcademico:
    def __init__(self, nombre_programa):
        self._nombre_programa = nombre_programa

    def get_nombre_programa(self):
        return self._nombre_programa

    def evaluar_aprobacion(self, notas):
        raise NotImplementedError("Método abstracto debe ser implementado en la subclase")

class Curso(ProgramaAcademico):
    def __init__(self):
        super().__init__("Curso")

    def evaluar_aprobacion(self, notas):
        if not notas:
            return False
        promedio = sum(notas) / len(notas)
        return promedio >= 10.0

class Diplomado(ProgramaAcademico):
    def __init__(self):
        super().__init__("Diplomado")

    def evaluar_aprobacion(self, notas):
        if not notas:
            return False
        promedio = sum(notas) / len(notas)
        return promedio >= 14.0

class Bootcamp(ProgramaAcademico):
    def __init__(self):
        super().__init__("Bootcamp")

    def evaluar_aprobacion(self, notas):
        if not notas:
            return False
        # Exige estrictamente que ninguna nota individual sea menor a 14/20
        return all(nota >= 14.0 for nota in notas)

# ==========================================
# JERARQUÍA DE PERSONAS
# ==========================================
class Persona:
    def __init__(self, cedula, nombre, correo):
        self._cedula = cedula
        self._nombre = nombre
        self._correo = correo

    def get_cedula(self):
        return self._cedula

    def get_nombre(self):
        return self._nombre

    def get_correo(self):
        return self._correo

    def mostrar_informacion(self):
        return f"ID: {self._cedula} | Nombre: {self._nombre} | Correo: {self._correo}"

class Alumno(Persona):
    def __init__(self, cedula, nombre, correo, programa):
        super().__init__(cedula, nombre, correo)
        self._programa = programa
        self._notas = []

    def agregar_nota(self, nota):
        if len(self._notas) < 3:
            self._notas.append(nota)
            return True
        return False

    def deshacer_nota(self):
        if self._notas:
            return self._notas.pop()
        return None

    def get_notas(self):
        return self._notas

    def get_programa(self):
        return self._programa

    def obtener_promedio(self):
        if not self._notas:
            return 0.0
        return sum(self._notas) / len(self._notas)

    def esta_aprobado(self):
        return self._programa.evaluar_aprobacion(self._notas)

    def mostrar_informacion(self):
        prom = self.obtener_promedio()
        estatus = "APROBADO" if self.esta_aprobado() else "REPROBADO"
        notas_str = ", ".join(map(str, self._notas)) if self._notas else "Sin notas"
        return f"[Alumno] {super().mostrar_informacion()} | Prog: {self._programa.get_nombre_programa()} | Notas: [{notas_str}] | Prom: {prom:.1f} | Estatus: {estatus}"

class Profesor(Persona):
    def __init__(self, cedula, nombre, correo, especialidad, materia_asignada):
        super().__init__(cedula, nombre, correo)
        self._especialidad = especialidad
        self._materia_asignada = materia_asignada

    def get_especialidad(self):
        return self._especialidad

    def get_materia_asignada(self):
        return self._materia_asignada

    def mostrar_informacion(self):
        return f"[Profesor] {super().mostrar_informacion()} | Esp: {self._especialidad} | Materia: {self._materia_asignada}"

# ==========================================
# CLASE DE GESTIÓN CENTRAL Y PERSISTENCIA
# ==========================================
class SistemaGestionAcademica:
    def __init__(self):
        self.alumnos = []
        self.profesores = []
        self.pila_comandos = []  # Estructura LIFO para deshacer nota (guarda tuplas: (cedula, nota))
        self.cola_certificados = []  # Estructura FIFO para la cola de graduandos

        self.archivo_alumnos = "alumnos.txt"
        self.archivo_profesores = "profesores.txt"
        self.archivo_certificados = "certificados_pendientes.txt"

        self.cargar_archivos_txt()

    def registrar_alumno(self):
        print("\n--- Registrar Alumno ---")
        cedula = input("Cédula/ID: ").strip()
        nombre = input("Nombre Completo: ").strip()
        correo = input("Correo Electrónico: ").strip()
        print("Seleccione Programa: 1. Curso | 2. Diplomado | 3. Bootcamp")
        opc_prog = input("Opción (1-3): ").strip()

        if opc_prog == "1":
            prog = Curso()
        elif opc_prog == "2":
            prog = Diplomado()
        elif opc_prog == "3":
            prog = Bootcamp()
        else:
            print("❌ Opción de programa inválida.")
            return

        alumno = Alumno(cedula, nombre, correo, prog)
        self.alumnos.append(alumno)
        self.guardar_archivos_txt()
        print("✅ Alumno registrado exitosamente.")

    def registrar_profesor(self):
        print("\n--- Registrar Profesor ---")
        cedula = input("Cédula/ID: ").strip()
        nombre = input("Nombre Completo: ").strip()
        correo = input("Correo Electrónico: ").strip()
        especialidad = input("Especialidad: ").strip()
        materia = input("Materia Asignada: ").strip()

        profesor = Profesor(cedula, nombre, correo, especialidad, materia)
        self.profesores.append(profesor)
        self.guardar_archivos_txt()
        print("✅ Profesor registrado exitosamente.")

    def registrar_nota(self):
        print("\n--- Registrar Nota ---")
        cedula = input("Ingrese Cédula del Alumno: ").strip()
        alumno = next((a for a in self.alumnos if a.get_cedula() == cedula), None)

        if not alumno:
            print("❌ Alumno no encontrado.")
            return

        try:
            nota = float(input("Ingrese nota (0 - 20): "))
            if nota < 0 or nota > 20:
                print("❌ La nota debe estar entre 0 y 20.")
                return
            
            if alumno.agregar_nota(nota):
                self.pila_comandos.append((cedula, nota))  # Apilar acción LIFO
                self.guardar_archivos_txt()
                print("✅ Nota registrada exitosamente.")
            else:
                print("❌ El alumno ya tiene el máximo de 3 notas ingresadas.")
        except ValueError:
            print("❌ Error: Ingrese un valor numérico válido.")

    def deshacer_ultima_nota(self):
        print("\n--- Deshacer Último Registro de Nota (LIFO) ---")
        if not self.pila_comandos:
            print("⚠️ No hay acciones recientes para deshacer.")
            return

        cedula, nota_eliminada = self.pila_comandos.pop()  # LIFO
        alumno = next((a for a in self.alumnos if a.get_cedula() == cedula), None)

        if alumno:
            alumno.deshacer_nota()
            self.guardar_archivos_txt()
            print(f"✅ Se removió la nota {nota_eliminada} del alumno {alumno.get_nombre()}.")

    def generar_cola_certificados(self):
        print("\n--- Generar Cola de Certificados (FIFO) ---")
        self.cola_certificados.clear()

        # Filtrar solo alumnos aprobados
        for alumno in self.alumnos:
            if alumno.esta_aprobado():
                self.cola_certificados.append(alumno)  # Encolar FIFO

        print(f" Total graduandos procesados en cola: {len(self.cola_certificados)}")
        
        # Exportar a certificados_pendientes.txt
        with open(self.archivo_certificados, "w", encoding="utf-8") as f:
            f.write("=========================================\n")
            f.write("REPORTE DE CERTIFICADOS PENDIENTES\n")
            f.write("=========================================\n")
            f.write(f"Total de graduandos en cola: {len(self.cola_certificados)}\n\n")

            for idx, al in enumerate(self.cola_certificados, 1):
                f.write(f"{idx}. [{al.get_cedula()}] {al.get_nombre()}\n")
                f.write(f"   - Programa: {al.get_programa().get_nombre_programa()}\n")
                f.write(f"   - Promedio Final: {al.obtener_promedio():.1f}\n")
                f.write(f"   - Estatus: APROBADO\n\n")

            f.write("=========================================\n")
            f.write("* Fin del reporte - Generado por SGA-DO *\n")

        print(f"✅ Archivo '{self.archivo_certificados}' generado con éxito.")

    def mostrar_reporte_general(self):
        print("\n=========================================")
        print("          REPORTE GENERAL SGA-DO         ")
        print("=========================================")
        print("\n--- PROFESORES ACTIVOS ---")
        if not self.profesores:
            print("No hay profesores registrados.")
        for p in self.profesores:
            print(p.mostrar_informacion())

        print("\n--- ALUMNOS REGISTRADOS ---")
        if not self.alumnos:
            print("No hay alumnos registrados.")
        for a in self.alumnos:
            print(a.mostrar_informacion())
        print("=========================================\n")

    def guardar_archivos_txt(self):
        with open(self.archivo_alumnos, "w", encoding="utf-8") as f:
            for a in self.alumnos:
                notas = a.get_notas()
                n1 = notas[0] if len(notas) > 0 else 0
                n2 = notas[1] if len(notas) > 1 else 0
                n3 = notas[2] if len(notas) > 2 else 0
                prog_nombre = a.get_programa().get_nombre_programa()
                f.write(f"{a.get_cedula()},{a.get_nombre()},{a.get_correo()},{prog_nombre},{n1},{n2},{n3}\n")

        with open(self.archivo_profesores, "w", encoding="utf-8") as f:
            for p in self.profesores:
                f.write(f"{p.get_cedula()},{p.get_nombre()},{p.get_correo()},{p.get_especialidad()},{p.get_materia_asignada()}\n")

    def cargar_archivos_txt(self):
        if os.path.exists(self.archivo_alumnos):
            with open(self.archivo_alumnos, "r", encoding="utf-8") as f:
                for linea in f:
                    partes = linea.strip().split(",")
                    if len(partes) >= 7:
                        ced, nom, corr, prog_tipo, n1, n2, n3 = partes[:7]
                        if prog_tipo == "Curso":
                            prog = Curso()
                        elif prog_tipo == "Diplomado":
                            prog = Diplomado()
                        else:
                            prog = Bootcamp()

                        al = Alumno(ced, nom, corr, prog)
                        for n in [float(n1), float(n2), float(n3)]:
                            if n > 0:
                                al.agregar_nota(n)
                        self.alumnos.append(al)

        if os.path.exists(self.archivo_profesores):
            with open(self.archivo_profesores, "r", encoding="utf-8") as f:
                for linea in f:
                    partes = linea.strip().split(",")
                    if len(partes) >= 5:
                        ced, nom, corr, esp, mat = partes[:5]
                        self.profesores.append(Profesor(ced, nom, corr, esp, mat))


# ==========================================
# MENÚ INTERACTIVO PRINCIPAL
# ==========================================
def main():
    sistema = SistemaGestionAcademica()

    while True:
        print("\n=========================================")
        print("     SGA-DO: SISTEMA DIPLOMADOSONLINE    ")
        print("=========================================")
        print("1. Registrar Alumno")
        print("2. Registrar Profesor")
        print("3. Registrar Notas a un Alumno")
        print("4. Deshacer Último Registro de Nota")
        print("5. Generar Cola de Certificados")
        print("6. Mostrar Reporte General")
        print("7. Salir")

        opcion = input("Seleccione una opción (1-7): ").strip()

        if opcion == "1":
            sistema.registrar_alumno()
        elif opcion == "2":
            sistema.registrar_profesor()
        elif opcion == "3":
            sistema.registrar_nota()
        elif opcion == "4":
            sistema.deshacer_ultima_nota()
        elif opcion == "5":
            sistema.generar_cola_certificados()
        elif opcion == "6":
            sistema.mostrar_reporte_general()
        elif opcion == "7":
            print("\n💾 Guardando datos y cerrando sistema de forma segura. ¡Hasta luego!")
            break
        else:
            print("❌ Opción inválida. Intente de nuevo.")

if __name__ == "__main__":
    main()