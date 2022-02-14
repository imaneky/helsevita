package com.urjc.es.helsevita.Controllers;

import com.urjc.es.helsevita.Entities.Appointment;
import com.urjc.es.helsevita.Entities.HealthPersonnel;
import com.urjc.es.helsevita.Entities.Patient;
import com.urjc.es.helsevita.Entities.Question;
import com.urjc.es.helsevita.Enums.EnumRoles;
import com.urjc.es.helsevita.Exceptions.*;
import com.urjc.es.helsevita.Repositories.AdminRepository;
import com.urjc.es.helsevita.Services.AppointmentService;
import com.urjc.es.helsevita.Services.HealthPersonnelService;
import com.urjc.es.helsevita.Services.PatientService;
import com.urjc.es.helsevita.Services.QuestionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

@Controller
public class WebController {
    CsrfToken token;
    private static final String tokenstr = "token";
    private static final String userstr = "user";
    private static final String objectstr = "object";
    private static final String csrfstr = "_csrf";
    private static final String usernamestr = "username";
    private static final String errorstr = "error";
    private static final String questionstr = "question";
    private static final String idpatientstr = "id_paciente";


    @Autowired
    PatientService patientService;

    @Autowired
    HealthPersonnelService healthPersonnelService;

    @Autowired
    AppointmentService appointmentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    AdminRepository adminRepository;

    Appointment appointmentToEngage;



    @RequestMapping("/")
    ModelAndView index(HttpServletRequest request, Model model) {
        var a = SecurityContextHolder.getContext().getAuthentication();
        var username = a.getName();
        token = (CsrfToken) request.getAttribute(csrfstr);
        model.addAttribute(tokenstr, token.getToken());
        if (username.equals("anonymousUser")) {
            return new ModelAndView("index");
        } else {
            var mv = new ModelAndView("indexAuth");
            mv.addObject(userstr, username);
            return mv;
        }

    }

    @GetMapping("/loginError")
    public String loginerror(HttpServletRequest request) {
        return "loginerror";
    }

    @RequestMapping("/mostrar/{id}")
    ModelAndView view(@PathVariable Integer id, HttpServletRequest request) {
        Patient temp = patientService.returnPatient(id);
        var mv = new ModelAndView("mostrar");
        mv.addObject(userstr, temp);
        return mv;
    }


    @GetMapping({"/searchHealthPersonnel"})
    public String healthPersonnelList(Model model, @RequestParam(name = "q1", required = false) String query, @RequestParam(name = "q2", required = false) String query2, HttpServletRequest request) {
        boolean b1 = false;
        boolean b2 = false;
        List<HealthPersonnel> result = null;
        List<HealthPersonnel> result2 = null;
        List<HealthPersonnel> miLista;
        if (query != null) {
            result = healthPersonnelService.search(query);
            b1 = true;
        }
        if (query2 != null) {
            result2 = healthPersonnelService.searchByAge(query2);
            b2 = true;
        }
        if (b1 && b2) {
            miLista = intersectionH(result, result2);
        } else if (b1) {
            miLista = result;
        } else if (b2) {
            miLista = result2;
        } else {
            miLista = healthPersonnelService.returnAllHealthPersonnels();
        }

        if (result2 == null) {
            miLista = result;
        }
        model.addAttribute(objectstr, miLista);
        return "buscarSanitario";
    }


    //Call to the exception
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView exception(UserNotFoundException e, HttpServletRequest request) {
        var mv = new ModelAndView("user-not-found");
        mv.addObject(usernamestr, e.getUsername());
        return mv;
    }

    @ExceptionHandler(IncorrectSearchParametersException.class)
    public ModelAndView exception2(IncorrectSearchParametersException e, HttpServletRequest request) {
        return new ModelAndView("incorrect-parameters");
    }


    @ExceptionHandler(AppointmentAlreadyExistsException.class)
    public ModelAndView exception5(AppointmentAlreadyExistsException e, HttpServletRequest request) {
        var mv = new ModelAndView("appointmentAlreadyExists");
        var temp = e.getAppointment();
        mv.addObject("dia", temp.getDay());
        mv.addObject("mes", temp.getMonth());
        mv.addObject("year", temp.getYear());
        mv.addObject("hora", temp.getHour());
        mv.addObject("minuto", temp.getMinute());
        return mv;

    }


    @GetMapping("/addAppointment/{id}")
    public ModelAndView addAppointment(Model model, @PathVariable Integer id, HttpServletRequest request) {

        var temp = patientService.returnPatient(id);
        var mv = new ModelAndView("appointment");
        mv.addObject("paciente", temp);
        return mv;
    }


    @RequestMapping("/whichDoc")
    public ModelAndView addAppointmentCode(@RequestParam Map<String, String> requestParams, HttpServletRequest request) {
        var idPatient = Integer.parseInt(requestParams.get(idpatientstr));
        var text = requestParams.get("tiempo");
        int year = Integer.parseInt((String) text.subSequence(0, 4));
        int month = Integer.parseInt((String) text.subSequence(5, 7));
        int day = Integer.parseInt((String) text.subSequence(8, 10));
        int hour = Integer.parseInt((String) text.subSequence(11, 13));
        int minute = Integer.parseInt((String) text.subSequence(14, 16));
        var patient = patientService.returnPatient(idPatient);

        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);
        Appointment temp = this.appointmentToEngage = new Appointment(hour, minute, day, month, year, null, patient);


        var lista = healthPersonnelService.returnHealthPersonnelsByPatient(patientList);
        var mv = new ModelAndView("cualDoctor");
        mv.addObject("cita", temp);
        mv.addObject("docs", lista);
        mv.addObject("paciente", patient);
        return mv;
    }

    @RequestMapping("/exito")
    public ModelAndView exito(@RequestParam Map<String, String> requestParams, HttpServletRequest request) {
        int idDoctor = Integer.parseInt(requestParams.get("id_doctor"));
        int idPatient = Integer.parseInt(requestParams.get(idpatientstr));
        var paciente = patientService.returnPatient(idPatient);
        List<Appointment> appointmentList = paciente.getAppointments();
        var text = requestParams.get("tiempo");
        int year = Integer.parseInt((String) text.subSequence(0, 4));
        int month = Integer.parseInt((String) text.subSequence(5, 7));
        int day = Integer.parseInt((String) text.subSequence(8, 10));
        int hour = Integer.parseInt((String) text.subSequence(11, 13));
        int minute = Integer.parseInt((String) text.subSequence(14, 16));
        var doctor = healthPersonnelService.returnHealthPersonnel(idDoctor);
        Appointment temp = new Appointment(hour, minute, day, month, year, doctor, paciente);;
        for (Appointment temp2 : appointmentList) {
            if ((temp2.getYear().equals(temp.getYear())) && (temp2.getMonth().equals(temp.getMonth())) && (temp2.getDay().equals(temp.getDay()))
                    && (temp2.getHour().equals(temp.getHour())) && (temp2.getMinute().equals(temp.getMinute()))) {

                throw new AppointmentAlreadyExistsException(temp2);
            }
        }


        return new ModelAndView("exito");
    }

    @RequestMapping("/crearSanitario")
    public ModelAndView crearSanitario(HttpServletRequest request) {
        List<String> cosas = new ArrayList<>();
        var lista = EnumRoles.rolValues;
        for (EnumRoles c : EnumRoles.values())
            cosas.add(c.toString());

        var mv = new ModelAndView("crearSanitario");
        mv.addObject("roles", lista);
        return mv;
    }

    public List<Patient> union(List<Patient> list1, List<Patient> list2) {
        if (!(list1 == null || list2 == null)) {
            Set<Patient> set = new HashSet<>();

            set.addAll(list1);
            set.addAll(list2);

            return new ArrayList<>(set);
        }
        return Collections.emptyList();
    }

    public List<Patient> intersectionP(List<Patient> list1, List<Patient> list2) {
        if (!(list1 == null || list2 == null)) {
            List<Patient> list = new ArrayList<>();

            for (Patient t : list1) {
                if (list2.contains(t)) {
                    list.add(t);
                }
            }

            return list;
        }
        return Collections.emptyList();
    }

    public List<HealthPersonnel> intersectionH(List<HealthPersonnel> list1, List<HealthPersonnel> list2) {
        if (!(list1 == null || list2 == null)) {
            List<HealthPersonnel> list = new ArrayList<>();

            for (HealthPersonnel t : list1) {
                if (list2.contains(t)) {
                    list.add(t);
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        token = (CsrfToken) request.getAttribute(csrfstr);
        model.addAttribute(tokenstr, token.getToken());
        return "login";
    }

    @RequestMapping("/autenticacion")
    public ModelAndView autenticacion(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        Patient temp = patientService.returnPatientByUsername(username);
        if (new BCryptPasswordEncoder().matches(password, temp.getPassword())) {

            return new ModelAndView("exito");
        }
        return new ModelAndView(errorstr);
    }

    @RequestMapping("/loginExitoso")
    public ModelAndView loginExitoso() {
        return new ModelAndView("loginExito");
    }

    @RequestMapping("/areaPaciente")
    public String areaPaciente(HttpServletRequest request, Model model) {
        return "areaPaciente";
    }

    @RequestMapping("/areaSanitario")
    public String areaSanitario(HttpServletRequest request, Model model) {
        return "areaSanitario";
    }

    @RequestMapping("/citaAgregada")
    public String citaAgregada(HttpServletRequest request, Model model) {
        return "citaAgregada";
    }

    @RequestMapping("/contact-us")
    public String contactUs(HttpServletRequest request, Model model) {
        return "contact-us";
    }

    @RequestMapping("/crearPaciente")
    public String crearPaciente(HttpServletRequest request, Model model) {
        return "crearPaciente";
    }
    @RequestMapping("/areaAdmin")
    public String areaAdmin(){
        return "areaAdmin";
    }
    @RequestMapping("/faq")
    public String faq(HttpServletRequest request, Model model) {
        var lista = questionService.returnAll();
        for (var txt : lista){
            if (txt.getAnswer() == null){
                txt.setAnswer("No hay respuesta aún :(");
            }
            if (txt.getCosa().toLowerCase().contains("script") || txt.getCosa().toLowerCase().contains("%") ){
                txt.setCosa("A donde ibas crack");
            }
        }
        model.addAttribute(questionstr, lista);
        return "faq";
    }

    @RequestMapping("/insurance")
    public String insurance(HttpServletRequest request, Model model) {
        return "insurance";
    }

    @RequestMapping("/myHelsevita")
    public String myhelsevita(HttpServletRequest request, Model model) {
        token = (CsrfToken) request.getAttribute(csrfstr);
        model.addAttribute(tokenstr, token.getToken());
        return "myHelsevita";
    }

    @RequestMapping("/search-center")
    public String searchCenter(HttpServletRequest request, Model model) {
        return "search-center";
    }

    @RequestMapping("/work-with-us")
    public String workWithUs(HttpServletRequest request, Model model) {
        return "work-with-us";
    }

    @RequestMapping("/exito-contacto")
    public String exitoContacto(HttpServletRequest request, Model model) {
        return "exito-contacto";
    }

    @RequestMapping("/nuevaCita")
    public String nuevaCita(HttpServletRequest request, Model model) {
        Patient patient = patientService.returnPatientByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        var id = patient.getId();
        model.addAttribute(idpatientstr,id);
        List<Patient> lista = new ArrayList<>(); lista.add(patient);
        List<HealthPersonnel> docs = healthPersonnelService.returnHealthPersonnelsByPatient(lista);
        model.addAttribute("docs",docs);
        return "nuevaCita";
    }

    @RequestMapping("/mostrarCitas")
    public ModelAndView mostrarCitas(){
        Patient patient = patientService.returnPatientByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (patient != null){
            List<Appointment> citas = patient.getAppointments();
            appointmentService.returnAllAppointmentsOfPatient(patient);
            var mv = new ModelAndView("/mostrarCitas");
            mv.addObject("citas",citas);
            return mv;
        } else{
            throw new AppointmentNotFoundException();
        }
    }

    @GetMapping({"/searchPatient"})
    public String patientList(Model model, @RequestParam(name = "q1", required = false) String query, @RequestParam(name = "q2", required = false) String query2, HttpServletRequest request) {
        return getString(model, query, query2);
    }

    @RequestMapping("/mostrarPacientes")
    public String mostrarPacientes(Model model, HttpServletRequest request){
        HealthPersonnel healthPersonnel = healthPersonnelService.returnHealthPersonnelByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (healthPersonnel != null){
            var lista = patientService.returnAllPatientsByHealthPersonnel(healthPersonnel);
            Set<Patient> set = new HashSet<>(lista);
            model.addAttribute("pacientes",set);
            return "mostrarPacientes";
        }
        return errorstr;
    }
    @RequestMapping("/admin/mostrarPacientes")
    public String mostrarPacientesAdmin(Model model, @RequestParam(name = "q1", required = false) String query, @RequestParam(name = "q2", required = false) String query2,HttpServletRequest request){
        return getString(model, query, query2);
    }

    @NotNull
    private String getString(Model model, @RequestParam(name = "q1", required = false) String query, @RequestParam(name = "q2", required = false) String query2) {
        boolean b1 = false;
        boolean b2 = false;
        List<Patient> result = null;
        List<Patient> result2 = null;
        List<Patient> myList;
        if (query != null) {
            result = (List<Patient>) patientService.search(query);
            b1 = true;
        }
        if (query2 != null) {
            result2 = patientService.searchByAge(query2);
            b2 = true;
        }
        if (b1 && b2) {
            myList = intersectionP(result, result2);
        } else if (b1) {
            myList = result;
        } else if (b2) {
            myList = result2;
        } else {
            myList = (List<Patient>) patientService.returnAllPatients();
        }

        if (result2 == null) {
            myList = result;
        }
        model.addAttribute(objectstr, myList);
        return "buscarPaciente";
    }

    @RequestMapping("/admin/mostrarSanitarios")
    public String mostrarSanitariosAdmin(Model model, @RequestParam(name = "q1", required = false) String query, @RequestParam(name = "q2", required = false) String query2,HttpServletRequest request){
        boolean b1 = false;
        boolean b2 = false;
        List<HealthPersonnel> result = null;
        List<HealthPersonnel> result2 = null;
        List<HealthPersonnel> myList;
        if (query != null) {
            result = healthPersonnelService.search(query);
            b1 = true;
        }
        if (query2 != null) {
            result2 = healthPersonnelService.searchByAge(query2);
            b2 = true;
        }
        if (b1 && b2) {
            myList = intersectionH(result, result2);
        } else if (b1) {
            myList = result;
        } else if (b2) {
            myList = result2;
        } else {
            myList =  healthPersonnelService.returnAllHealthPersonnels();
        }

        if (result2 == null) {
            myList = result;
        }
        System.out.println(myList);
        model.addAttribute(objectstr, myList);
        return "buscarSanitario";
    }


    @RequestMapping("/performLogout")
    public String logOut(){
        return "logout";
    }

    @RequestMapping("/preguntasSinContestar")
    public ModelAndView preguntasSinContestar(HttpServletRequest request){
        List<Question> questions = questionService.returnAll();
        Set <Question> set = new HashSet<>();
        for (var text: questions){
            if(text.getAnswer()==null){
                set.add(text);
            }
        }
        var mv = new ModelAndView("preguntasSinContestar");
        mv.addObject(questionstr,set);
        return mv;
    }

    @RequestMapping("/contestarPregunta/{id}")
    public ModelAndView contestarPregunta(HttpServletRequest request, @PathVariable Integer id){
        Question q = questionService.returnQuestion(id);
        var mv = new ModelAndView("contestarPregunta");
        mv.addObject(questionstr,q);
        return mv;
    }


    @RequestMapping("/politica")
    public String politica(){
        return "politica";
    }

    @RequestMapping("/myProfile")
    public String profile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("anonymousUser")){
            if (patientService.returnPatientByUsername(username) != null){
                var paciente = patientService.returnPatientByUsername(username);
                model.addAttribute(usernamestr,paciente.getUsername());
                model.addAttribute("name",paciente.getName());
                model.addAttribute("surname1",paciente.getSurname1());
                model.addAttribute("surname2",paciente.getSurname2());
                model.addAttribute("dni",paciente.getDni());
                model.addAttribute("age",paciente.getAge());
                return "my-profile";
            }
            if (healthPersonnelService.returnHealthPersonnelByUsername(username) != null){
                var sanitario = healthPersonnelService.returnHealthPersonnelByUsername(username);
                model.addAttribute(usernamestr,sanitario.getUsername());
                model.addAttribute("name",sanitario.getName());
                model.addAttribute("surname1",sanitario.getSurname1());
                model.addAttribute("surname2",sanitario.getSurname2());
                model.addAttribute("dni",sanitario.getDni());
                model.addAttribute("age",sanitario.getAge());
                model.addAttribute("speciality",sanitario.getSpeciality());

                return "my-profile-sanitario";
            }
            if (adminRepository.findByUsername(username) != null){
                var admin = adminRepository.findByUsername(username);
                model.addAttribute(usernamestr,admin.getUsername());
                return "my-profile-admin";
            }
        }
        return errorstr;
    }

}